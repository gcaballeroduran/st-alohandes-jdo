with tab as(
select count(p.dias_reservados) as m
 FROM  a_Propiedad p
INNER JOIN   a_Habitacion hab ON(p.id = hab.id)
INNER JOIN   a_Apartamento ap ON(p.id = ap.id)
INNER JOIN   a_ReservaHabitacion resha ON(hab.id = resha.id_Habitacion)
INNER JOIN  a_ReservaApartamento resap ON(ap.id = resap.id_Apartamento)
INNER JOIN  a_Reserva res ON(res.id = resha.id_Reserva AND res.id = resap.id_Reserva)
WHERE tiempo >= res.fecha_Inicio AND tiempo <= res.fecha_ AND tipo = hab.tipo
)

        select min(t.m), p.id , p.capacidad, p.tamanio, p.dias_reservados, p.fecha_creacion, p.piso
		FROM tab t, a_Propiedad p 
        group by p.id, p.capacidad, p.tamanio, p.dias_reservados, p.fecha_creacion, p.piso
        

