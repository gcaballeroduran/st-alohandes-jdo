-- En caso de  que la reserva sea en un 
SELECT count(c.reservas) as res, p.id, p.capacidad, p.precio, p.tamanio, p.diasReservados, p.piso, p.habilitada
p.fechaCreacion, h.individual, h.esquema, h.tipo, a.amueblado, a.habitaciones, a.descripcionMenaje, a.tieneSeguro
FROM A_Cliente c NATURAL INNER JOIN A_PROPIEDAD p, A_HABITACION h, A_APARTAMENTO a, 
A_RESERVAHABITACION rh, A_RESESRVAAPARTAMENTO ra

WHERE p.id = #pIdProp AND r.FechaInicio BETWEEN TO_DATE(#pFI) AND TO_DATE (#pFF) AND res=0
ORDERED BY #pOrden