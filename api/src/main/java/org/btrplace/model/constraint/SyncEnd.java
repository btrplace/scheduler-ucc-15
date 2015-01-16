/*
 * Copyright (c) 2014 University Nice Sophia Antipolis
 *
 * This file is part of btrplace.
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 3 of the License, or (at your option) any later version.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package org.btrplace.model.constraint;

import org.btrplace.model.Node;
import org.btrplace.model.VM;

import java.util.*;

/**
 * A constraint to force some vms migration to terminate
 * at the same time.
 * <p>
 * Created by vkherbac on 01/09/14.
 */
public class SyncEnd extends SatConstraint {

    /**
     * Make a new constraint.
     *
     * @param vms the vms to sync
     */
    public SyncEnd(Collection<VM> vms) {
        super(vms, Collections.<Node>emptyList(), true);
    }

    public SyncEnd(VM... vms) {
        super(Arrays.asList(vms), Collections.<Node>emptyList(), true);
    }

    @Override
    public boolean setContinuous(boolean b) {
        return b;
    }

    @Override
    public SatConstraintChecker getChecker() {
        return new SyncEndChecker(this);
    }

    @Override
    public String toString() {
        return "syncEnd(" + "vm=" + getInvolvedVMs() + ", " + restrictionToString() + ")";
    }
}
