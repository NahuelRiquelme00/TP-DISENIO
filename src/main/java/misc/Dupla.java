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
public class Dupla<T1, T2>
{
    public T1 primero;
    public T2 segundo;

    public Dupla() 
    {
        this.primero = null;
        this.segundo = null;
    }
    
    public Dupla(T1 primero, T2 segundo) 
    {
        this.primero = primero;
        this.segundo = segundo;
    }

    @Override
    public int hashCode() 
    {
        int hash = 7;
        hash = 53 * hash + Objects.hashCode(this.primero);
        hash = 53 * hash + Objects.hashCode(this.segundo);
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
        final Dupla<?, ?> other = (Dupla<?, ?>) obj;
        if (!Objects.equals(this.primero, other.primero)) {
            return false;
        }
        if (!Objects.equals(this.segundo, other.segundo)) {
            return false;
        }
        return true;
    }
    
    @Override
    public String toString()
    {
        return "(" + primero.toString() + ", " + segundo.toString() + ")";
    }
}
