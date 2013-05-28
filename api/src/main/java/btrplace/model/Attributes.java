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

package btrplace.model;

import java.util.Set;

/**
 * Allow to specify attributes related to managed elements.
 * Attributes are key/value pair, where values are Java primitives (long, double, String, boolean)
 *
 * @author Fabien Hermenier
 */
public interface Attributes extends Cloneable {

    /**
     * Put a boolean value.
     *
     * @param t the element type
     * @param e the element identifier
     * @param k the attribute identifier
     * @param b the value to set
     * @return {@code true} if a previous value was overridden
     */
    boolean put(ElementType t, int e, String k, boolean b);

    /**
     * Put a String value.
     *
     * @param t the element type
     * @param e the element identifier
     * @param k the attribute identifier
     * @param s the value to set
     * @return {@code true} if a previous value was overridden
     */
    boolean put(ElementType t, int e, String k, String s);

    /**
     * Put a long value.
     *
     * @param t the element type
     * @param e the element identifier
     * @param k the attribute identifier
     * @param l the value to set
     * @return {@code true} if a previous value was overridden
     */
    boolean put(ElementType t, int e, String k, long l);

    /**
     * Put a double value.
     *
     * @param t the element type
     * @param e the element identifier
     * @param k the attribute identifier
     * @param d the value to set
     * @return {@code true} if a previous value was overridden
     */
    boolean put(ElementType t, int e, String k, double d);

    /**
     * Get an attribute value as a simple Object.
     *
     * @param t the element type
     * @param e the element identifier
     * @param k the attribute value
     * @return the value if it has been stated. {@code null} otherwise
     */
    Object get(ElementType t, int e, String k);

    /**
     * Get an attribute value as a boolean.
     *
     * @param t the element type
     * @param e the element identifier
     * @param k the attribute value
     * @return the value if it has been stated. {@code null} otherwise
     */
    Boolean getBoolean(ElementType t, int e, String k);

    /**
     * Get an attribute value as a long.
     *
     * @param t the element type
     * @param e the element identifier
     * @param k the attribute value
     * @return the value if it has been stated. {@code null} otherwise
     */
    Long getLong(ElementType t, int e, String k);

    /**
     * Get an attribute value as a string.
     *
     * @param t the element type
     * @param e the element identifier
     * @param k the attribute value
     * @return the value if it has been stated. {@code null} otherwise
     */
    String getString(ElementType t, int e, String k);

    /**
     * Get an attribute value as a double.
     *
     * @param t the element type
     * @param e the element identifier
     * @param k the attribute value
     * @return the value if it has been stated. {@code null} otherwise
     */
    Double getDouble(ElementType t, int e, String k);

    /**
     * Check if an attribute is set for a given element.
     *
     * @param t the element type
     * @param e the element
     * @param k the attribute identifier
     * @return {@code true} iff the attribute is set
     */
    boolean isSet(ElementType t, int e, String k);

    /**
     * Unset an attribute for a given element.
     *
     * @param t the element type
     * @param e the element identifier
     * @param k the attribute identifier
     * @return {@code true} iff a value was removed
     */
    boolean unset(ElementType t, int e, String k);

    /**
     * Clone the attributes.
     *
     * @return a new set of attributes
     */
    Attributes clone();

    /**
     * Get the elements having attributes defined.
     *
     * @return a set that may be empty
     */
    Set<Integer> getElements();

    /**
     * Get all the attributes keys that are registered for an element.
     *
     * @param u the element identifier
     * @return a set that may be empty
     */
    Set<String> getKeys(int u);

    /**
     * Put a value but try to cast into to a supported primitive if possible.
     * First, it tries to cast {@code v} first to a boolean, then to a long value,
     * finally to a double value. If none of the cast succeeded, the value is let
     * as a string.
     *
     * @param u the element identifier
     * @param k the attribute identifier
     * @param v the value to set
     * @return {@code true} if a previous value was overridden
     */
    boolean castAndPut(int u, String k, String v);

    /**
     * Remove all the attributes.
     */
    void clear();
}
