/*
 * Copyright (c) 2012 University of Nice Sophia-Antipolis
 *
 * This file is part of btrplace.
 *
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

import btrplace.model.*;
import btrplace.model.constraint.Offline;
import btrplace.plan.ReconfigurationPlan;
import btrplace.plan.action.ShutdownNode;
import btrplace.solver.SolverException;
import btrplace.solver.choco.DefaultChocoReconfigurationAlgorithm;
import btrplace.solver.choco.durationEvaluator.ConstantDuration;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.util.Collection;
import java.util.Collections;
import java.util.UUID;

/**
 * Unit tests for {@link COffline}.
 *
 * @author Fabien Hermenier
 */
public class COfflineTest {

    /**
     * Simple test, no VMs.
     */
    @Test
    public void simpleTest() throws SolverException {
        Mapping map = new DefaultMapping();
        UUID n1 = UUID.randomUUID();
        UUID n2 = UUID.randomUUID();
        map.addOnlineNode(n1);
        map.addOnlineNode(n2);

        Model model = new DefaultModel(map);
        DefaultChocoReconfigurationAlgorithm cra = new DefaultChocoReconfigurationAlgorithm();
        cra.getDurationEvaluators().register(ShutdownNode.class, new ConstantDuration(10));
        cra.setTimeLimit(-1);
        Collection<SatConstraint> x = Collections.singleton((SatConstraint) new Offline(map.getAllNodes()));
        ReconfigurationPlan plan = cra.solve(model, x);
        Assert.assertEquals(2, plan.size());
        Assert.assertEquals(10, plan.getDuration());
        Model res = plan.getResult();
        Assert.assertEquals(2, res.getMapping().getOfflineNodes().size());
    }
}
