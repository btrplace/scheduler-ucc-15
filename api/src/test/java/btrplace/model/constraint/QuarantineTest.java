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

package btrplace.model.constraint;

import btrplace.model.DefaultMapping;
import btrplace.model.DefaultModel;
import btrplace.model.Mapping;
import btrplace.model.Model;
import btrplace.plan.DefaultReconfigurationPlan;
import btrplace.plan.ReconfigurationPlan;
import btrplace.plan.event.BootVM;
import btrplace.plan.event.MigrateVM;
import btrplace.plan.event.ShutdownVM;
import btrplace.test.PremadeElements;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

;

/**
 * Unit tests for {@link Quarantine}.
 *
 * @author Fabien Hermenier
 */
public class QuarantineTest implements PremadeElements {

    @Test
    public void testInstantiation() {
        Set<Integer> s = new HashSet<>(Arrays.asList(n1, n2));
        Quarantine q = new Quarantine(s);
        Assert.assertNotNull(q.getChecker());
        Assert.assertTrue(q.getInvolvedVMs().isEmpty());
        Assert.assertEquals(q.getInvolvedNodes(), s);
        Assert.assertTrue(q.isContinuous());
        Assert.assertFalse(q.setContinuous(false));
        Assert.assertTrue(q.setContinuous(true));
        Assert.assertFalse(q.toString().contains("null"));
//        Assert.assertEquals(q.isSatisfied(new DefaultModel(new DefaultMapping())), SatConstraint.Sat.UNDEFINED);
        System.out.println(q);
    }

    @Test
    public void testEqualsHashCode() {
        Set<Integer> s = new HashSet<>(Arrays.asList(n1, n2));
        Quarantine q = new Quarantine(s);
        Assert.assertTrue(q.equals(q));
        Assert.assertTrue(q.equals(new Quarantine(new HashSet<>(s))));
        Assert.assertEquals(q.hashCode(), new Quarantine(new HashSet<>(s)).hashCode());
        Assert.assertFalse(q.equals(new Quarantine(new HashSet<Integer>())));
    }

    @Test
    public void testContinuousIsSatisfied() {
        Mapping map = new DefaultMapping();
        map.addOnlineNode(n1);
        map.addOnlineNode(n2);
        map.addOnlineNode(n3);
        map.addRunningVM(vm1, n1);
        map.addRunningVM(vm2, n2);
        map.addReadyVM(vm3);
        map.addRunningVM(vm4, n3);

        Quarantine q = new Quarantine(new HashSet<>(Arrays.asList(n1, n2)));

        Model mo = new DefaultModel(map);
        ReconfigurationPlan plan = new DefaultReconfigurationPlan(mo);
        Assert.assertEquals(q.isSatisfied(plan), true);
        plan.add(new ShutdownVM(vm2, n2, 1, 2));
        Assert.assertEquals(q.isSatisfied(plan), true);

        plan.add(new BootVM(vm3, n1, 0, 1));
        Assert.assertEquals(q.isSatisfied(plan), false);

        plan = new DefaultReconfigurationPlan(mo);
        plan.add(new BootVM(vm3, n3, 0, 1));
        Assert.assertEquals(q.isSatisfied(plan), true);
        plan.add(new MigrateVM(vm4, n3, n2, 0, 1));
        Assert.assertEquals(q.isSatisfied(plan), false);

        plan = new DefaultReconfigurationPlan(mo);
        plan.add(new MigrateVM(vm2, n2, n1, 0, 1));
        Assert.assertEquals(q.isSatisfied(plan), false);


    }
}
