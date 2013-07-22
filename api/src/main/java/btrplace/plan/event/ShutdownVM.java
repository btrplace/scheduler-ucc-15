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

package btrplace.plan.event;


import btrplace.model.Mapping;
import btrplace.model.Model;
import btrplace.model.Node;
import btrplace.model.VM;

import java.util.Objects;

/**
 * An action to stop a virtual machine running on an online node and put it into the ready state.
 *
 * @author Fabien Hermenier
 */
public class ShutdownVM extends Action implements VMStateTransition {

    private VM vm;

    private Node node;

    /**
     * Make a new action.
     *
     * @param vm    the virtual machine to stop
     * @param on    the hosting node
     * @param start the moment the action starts
     * @param end   the moment the action finish
     */
    public ShutdownVM(VM vm, Node on, int start, int end) {
        super(start, end);
        this.vm = vm;
        this.node = on;
    }


    /**
     * Apply the action by removing the virtual machine from the model.
     *
     * @param m the model to alter
     * @return {@code true}
     */
    @Override
    public boolean applyAction(Model m) {
        Mapping map = m.getMapping();
        if (map.isOnline(node) &&
                map.isRunning(vm) &&
                map.getVMLocation(vm) == node) {
            map.addReadyVM(vm);
            return true;
        }
        return false;
    }

    @Override
    public String pretty() {
        return new StringBuilder("shutdown(")
                .append("vm=").append(vm)
                .append(", on=").append(node).append(')').toString();
    }


    @Override
    public boolean equals(Object o) {
        if (o == null) {
            return false;
        } else if (o == this) {
            return true;
        } else if (o.getClass() == this.getClass()) {
            ShutdownVM that = (ShutdownVM) o;
            return this.vm.equals(that.vm) &&
                    this.node.equals(that.node) &&
                    this.getStart() == that.getStart() &&
                    this.getEnd() == that.getEnd();
        }
        return false;
    }

    @Override
    public int hashCode() {
        return Objects.hash(getStart(), getEnd(), vm, node);
    }

    @Override
    public VM getVM() {
        return vm;
    }

    /**
     * Get the node hosting the VM.
     *
     * @return the node identifier
     */
    public Node getNode() {
        return node;
    }


    @Override
    public VMState getCurrentState() {
        return VMState.running;
    }

    @Override
    public VMState getNextState() {
        return VMState.ready;
    }

    @Override
    public Object visit(ActionVisitor v) {
        return v.visit(this);
    }
}
