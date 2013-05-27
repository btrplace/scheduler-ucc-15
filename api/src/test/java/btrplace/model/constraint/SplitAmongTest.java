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
import btrplace.plan.event.MigrateVM;
import btrplace.test.PremadeElements;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

;

/**
 * Unit tests for {@link SplitAmong}.
 *
 * @author Fabien Hermenier
 */
public class SplitAmongTest implements PremadeElements {

    @Test
    public void testInstantiation() {

        Set<Integer> vs1 = new HashSet<>(Arrays.asList(vm1, vm2));
        Set<Integer> vs2 = new HashSet<>(Arrays.asList(vm3, vm4));

        Set<Set<Integer>> vGrps = new HashSet<>(Arrays.asList(vs1, vs2));


        Set<Integer> ps1 = new HashSet<>(Arrays.asList(n1, n2));
        Set<Integer> ps2 = new HashSet<>(Arrays.asList(n3, n4));
        Set<Set<Integer>> pGrps = new HashSet<>(Arrays.asList(ps1, ps2));

        SplitAmong sp = new SplitAmong(vGrps, pGrps);
        Assert.assertNotNull(sp.getChecker());
        Assert.assertEquals(sp.getGroupsOfVMs(), vGrps);
        Assert.assertEquals(sp.getGroupsOfNodes(), pGrps);
        Assert.assertTrue(sp.getInvolvedVMs().containsAll(vs1));
        Assert.assertTrue(sp.getInvolvedVMs().containsAll(vs2));
        Assert.assertTrue(sp.getInvolvedNodes().containsAll(ps1));
        Assert.assertTrue(sp.getInvolvedNodes().containsAll(ps2));
        System.out.println(sp.toString());

        Assert.assertFalse(sp.isContinuous());
        Assert.assertTrue(sp.setContinuous(true));
        Assert.assertTrue(sp.isContinuous());

        Assert.assertTrue(sp.setContinuous(false));
        Assert.assertFalse(sp.isContinuous());

        sp = new SplitAmong(vGrps, pGrps, true);
        Assert.assertTrue(sp.isContinuous());
    }

    @Test
    public void testEqualsAndHashCode() {

        Set<Integer> vs1 = new HashSet<>(Arrays.asList(vm1, vm2));
        Set<Integer> vs2 = new HashSet<>(Arrays.asList(vm3, vm4));
        Set<Set<Integer>> vGrps = new HashSet<>(Arrays.asList(vs1, vs2));


        Set<Integer> ps1 = new HashSet<>(Arrays.asList(n1, n2));
        Set<Integer> ps2 = new HashSet<>(Arrays.asList(n3, n4));
        Set<Set<Integer>> pGrps = new HashSet<>(Arrays.asList(ps1, ps2));

        SplitAmong sp = new SplitAmong(vGrps, pGrps);
        Assert.assertTrue(sp.equals(sp));
        Assert.assertTrue(sp.equals(new SplitAmong(vGrps, pGrps)));
        Assert.assertEquals(sp.hashCode(), new SplitAmong(vGrps, pGrps).hashCode());
        Assert.assertFalse(sp.equals(new SplitAmong(new HashSet<Set<Integer>>(), pGrps)));
        Assert.assertFalse(sp.equals(new SplitAmong(vGrps, new HashSet<Set<Integer>>())));

        SplitAmong sp2 = new SplitAmong(vGrps, pGrps);
        sp2.setContinuous(true);
        Assert.assertFalse(sp.equals(sp2));
    }

    @Test
    public void testDiscreteIsSatisfied() {

        Set<Integer> vs1 = new HashSet<>(Arrays.asList(vm1, vm2));
        Set<Integer> vs2 = new HashSet<>(Arrays.asList(vm3, vm4));
        Set<Set<Integer>> vGrps = new HashSet<>(Arrays.asList(vs1, vs2));


        Set<Integer> ps1 = new HashSet<>(Arrays.asList(n1, n2));
        Set<Integer> ps2 = new HashSet<>(Arrays.asList(n3, n4));
        Set<Set<Integer>> pGrps = new HashSet<>(Arrays.asList(ps1, ps2));

        Mapping map = new DefaultMapping();
        map.addOnlineNode(n1);
        map.addOnlineNode(n2);
        map.addOnlineNode(n3);
        map.addOnlineNode(n4);

        map.addRunningVM(vm1, n1);
        map.addRunningVM(vm2, n1);
        map.addRunningVM(vm3, n3);
        map.addRunningVM(vm4, n4);

        Model mo = new DefaultModel(map);

        SplitAmong sp = new SplitAmong(vGrps, pGrps);
        Assert.assertEquals(sp.isSatisfied(mo), true);

        //Spread over multiple groups, not allowed
        map.addRunningVM(vm2, n3);
        Assert.assertEquals(sp.isSatisfied(mo), false);
        //pGroup co-location. Not allowed
        map.addRunningVM(vm1, n3);
        map.addRunningVM(vm3, n4);
        Assert.assertEquals(sp.isSatisfied(mo), false);
    }

    @Test
    public void testContinuousIsSatisfied() {

        Set<Integer> vs1 = new HashSet<>(Arrays.asList(vm1, vm2));
        Set<Integer> vs2 = new HashSet<>(Arrays.asList(vm3, vm4));
        Set<Set<Integer>> vGrps = new HashSet<>(Arrays.asList(vs1, vs2));


        Set<Integer> ps1 = new HashSet<>(Arrays.asList(n1, n2));
        Set<Integer> ps2 = new HashSet<>(Arrays.asList(n3, n4));
        Set<Set<Integer>> pGrps = new HashSet<>(Arrays.asList(ps1, ps2));

        Mapping map = new DefaultMapping();
        map.addOnlineNode(n1);
        map.addOnlineNode(n2);
        map.addOnlineNode(n3);
        map.addOnlineNode(n4);

        map.addRunningVM(vm1, n1);
        map.addRunningVM(vm2, n1);
        map.addRunningVM(vm3, n3);
        map.addRunningVM(vm4, n4);

        Model mo = new DefaultModel(map);

        SplitAmong sp = new SplitAmong(vGrps, pGrps, true);
        ReconfigurationPlan plan = new DefaultReconfigurationPlan(mo);
        Assert.assertEquals(sp.isSatisfied(plan), true);

        plan.add(new MigrateVM(vm1, n1, n2, 3, 4));
        Assert.assertEquals(sp.isSatisfied(plan), true);

        map.addRunningVM(vm5, n4);
        Assert.assertEquals(sp.isSatisfied(plan), true);
        plan.add(new MigrateVM(vm2, n1, n3, 0, 2));
        Assert.assertEquals(sp.isSatisfied(plan), false);


    }
}
