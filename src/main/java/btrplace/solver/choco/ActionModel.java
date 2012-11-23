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

package btrplace.solver.choco;

import btrplace.plan.Action;
import choco.kernel.solver.variables.integer.IntDomainVar;

import java.util.List;

/**
 * Model an action.
 *
 * @author Fabien Hermenier
 */
public interface ActionModel {

    /**
     * Get the moment the action starts.
     *
     * @return a variable that must be positive
     */
    IntDomainVar getStart();

    /**
     * Get the moment the action ends.
     *
     * @return a variable that must be greater than {@link #getStart()}
     */
    IntDomainVar getEnd();

    /**
     * Get the action duration.
     *
     * @return a duration equals to {@code getEnd() - getStart()}
     */
    IntDomainVar getDuration();

    /**
     * Get the slice denoting the possible current placement of the subject on a node.
     *
     * @return a {@link Slice} that may be {@code null}
     */
    Slice getCSlice();

    /**
     * Get the slice denoting the possible future placement off the subject
     *
     * @return a {@link Slice} that may be {@code null}
     */
    Slice getDSlice();

    /**
     * Get the cost of the action.
     * TODO: Is it a required method ?
     *
     * @return a variable
     */
    IntDomainVar getCost();

    /**
     * Get the action that are generated for the model variables.
     *
     * @return a list of {@link Action} that may be empty.
     */
    List<Action> getResultingActions();

    /**
     * Get the next state of the subject manipulated by the action.
     *
     * @return {@code 0} for offline, {@code 1} for online.
     */
    IntDomainVar getState();

}
