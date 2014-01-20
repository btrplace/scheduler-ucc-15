/*
 * Copyright (c) 2013 University of Nice Sophia-Antipolis
 *
 * This file is part of btrplace.
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package btrplace.solver.choco.constraint;

import btrplace.model.Mapping;
import btrplace.model.Model;
import btrplace.model.Node;
import btrplace.model.VM;
import btrplace.model.constraint.Constraint;
import btrplace.model.constraint.Gather;
import btrplace.solver.choco.ReconfigurationProblem;
import btrplace.solver.choco.Slice;
import btrplace.solver.choco.actionModel.VMActionModel;
import solver.Solver;
import solver.constraints.IntConstraintFactory;
import solver.variables.IntVar;

import java.util.*;

/**
 * Choco implementation of {@link btrplace.model.constraint.Gather}.
 *
 * @author Fabien Hermenier
 */
public class CGather implements ChocoConstraint {

    private Gather cstr;

    /**
     * Make a new constraint.
     *
     * @param g the constraint to rely on
     */
    public CGather(Gather g) {
        cstr = g;
    }

    @Override
    public boolean inject(ReconfigurationProblem rp) {
        List<Slice> dSlices = new ArrayList<>();
        for (VM vm : cstr.getInvolvedVMs()) {
            VMActionModel a = rp.getVMAction(vm);
            Slice dSlice = a.getDSlice();
            if (dSlice != null) {
                dSlices.add(dSlice);
            }
        }
        if (cstr.isContinuous()) {
            //Check for the already running VMs
            Mapping map = rp.getSourceModel().getMapping();
            Node loc = null;
            for (VM vm : cstr.getInvolvedVMs()) {
                if (map.isRunning(vm)) {
                    Node node = map.getVMLocation(vm);
                    if (loc == null) {
                        loc = node;
                    } else if (!loc.equals(node)) {
                        rp.getLogger().error("Some VMs in '{}' are already running but not co-located", cstr.getInvolvedVMs());
                        return false;
                    }
                }
            }
            if (loc != null) {
                return placeDSlices(rp, dSlices, rp.getNode(loc));
            } else {
                return forceDiscreteCollocation(rp, dSlices);
            }
        }
        return forceDiscreteCollocation(rp, dSlices);
    }

    private boolean placeDSlices(ReconfigurationProblem rp, List<Slice> dSlices, int nIdx) {
        for (Slice s : dSlices) {
            if (!s.getHoster().instantiatedTo(nIdx)) {
                rp.getLogger().error("Unable to maintain the co-location of all the future-running VMs in '{}': ", cstr.getInvolvedVMs());
                return false;
            }
        }
        return true;
    }

    private boolean forceDiscreteCollocation(ReconfigurationProblem rp, List<Slice> dSlices) {
        Solver s = rp.getSolver();
        for (int i = 0; i < dSlices.size(); i++) {
            for (int j = 0; j < i; j++) {
                Slice s1 = dSlices.get(i);
                Slice s2 = dSlices.get(j);
                IntVar i1 = s1.getHoster();
                IntVar i2 = s2.getHoster();
                if (i1.instantiated() && i2.instantiated() && i1.getValue() != i2.getValue()) {
                    rp.getLogger().error("Unable to force VM '" + s1.getSubject() + "' to be co-located with VM '" + s2.getSubject() + "'");
                    return false;
                } else {
                    if (i1.instantiated()) {
                        if (!i2.instantiatedTo(i1.getValue())) {
                            rp.getLogger().error("Unable to force VM '" + s1.getSubject() + "' to be co-located with VM '" + s2.getSubject() + "'");
                            return false;
                        }
                    } else if (i2.instantiated()) {
                        if (!i1.instantiatedTo(i2.getValue())) {
                            rp.getLogger().error("Unable to force VM '" + s1.getSubject() + "' to be co-located with VM '" + s2.getSubject() + "'");
                            return false;
                        }
                    } else {
                        s.post(IntConstraintFactory.arithm(i1, "=", i2));//s.eq(i1, i2));
                    }
                }
            }
        }
        return true;
    }

    @Override
    public String toString() {
        return cstr.toString();
    }


    @Override
    public Set<VM> getMisPlacedVMs(Model m) {
        if (!cstr.isSatisfied(m)) {
            return new HashSet<>(cstr.getInvolvedVMs());
        }
        return Collections.emptySet();
    }

    /**
     * The builder associated to that constraint.
     */
    public static class Builder implements ChocoConstraintBuilder {
        @Override
        public Class<? extends Constraint> getKey() {
            return Gather.class;
        }

        @Override
        public CGather build(Constraint cstr) {
            return new CGather((Gather) cstr);
        }
    }
}
