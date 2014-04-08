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

package btrplace.solver.choco.view;

import btrplace.solver.SolverException;
import btrplace.solver.choco.ReconfigurationProblem;

/**
 * @author Fabien Hermenier
 */
public interface SolverViewBuilder {

    /**
     * Get the class of the {@link btrplace.model.view.ModelView} that is handled by this builder.
     *
     * @return a Class derived from {@link btrplace.model.view.ModelView}
     */
    String getKey();

    /**
     * Build the {@link ChocoView}
     *
     * @param rp the problem to addDim
     * @throws btrplace.solver.SolverException if an error occurred while building the view
     */
    ChocoView build(ReconfigurationProblem rp) throws SolverException;

}
