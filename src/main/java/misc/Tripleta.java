/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package misc;

import java.util.Objects;

/**
 *
 * @author Federico Pacheco
 */
public class Tripleta<T1, T2, T3> 
{
    public T1 primero;
    public T2 segundo;
    public T3 tercero;

    public Tripleta() 
    {
        this.primero = null;
        this.segundo = null;
        this.tercero = null;
    }
    
    public Tripleta(T1 primero, T2 segundo, T3 tercero) 
    {
        this.primero = primero;
        this.segundo = segundo;
        this.tercero = tercero;
    }

    @Override
    public int hashCode() 
    {
        int hash = 7;
        hash = 59 * hash + Objects.hashCode(this.primero);
        hash = 59 * hash + Objects.hashCode(this.segundo);
        hash = 59 * hash + Objects.hashCode(this.tercero);
        return hash;
    }

    @Override
    public boolean equals(Object obj) 
    {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Tripleta<?, ?, ?> other = (Tripleta<?, ?, ?>) obj;
        if (!Objects.equals(this.primero, other.primero)) {
            return false;
        }
        if (!Objects.equals(this.segundo, other.segundo)) {
            return false;
        }
        if (!Objects.equals(this.tercero, other.tercero)) {
            return false;
        }
        return true;
    }
    
    @Override
    public String toString()
    {
        return "(" + primero.toString() + ", " + segundo.toString() + ", " + tercero.toString() + ")";
    }
}
