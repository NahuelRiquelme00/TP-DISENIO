/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dao;

import entidades.PeriodoReserva;
import java.time.LocalDate;
import java.util.List;

/**
 *
 * @author Nahuel Riquelme
 */
public interface ReservaDAO {
    public List<PeriodoReserva> getPeriodosReservaEntreFechas(LocalDate fechaInicioGui, LocalDate fechaFinGui);
}
