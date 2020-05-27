-- En caso de  que la reserva sea en un HABITACION
SELECT count(c.reservas) as res, * 
FROM A_Cliente c NATURAL INNER JOIN A_PROPIEDAD p, A_HABITACION a, A_RESERVAHABITACION ra, 
WHERE p.id = #pIdProp AND r.FechaInicio BETWEEN TO_DATE(#pFI) AND TO_DATE (#pFF) AND res > 0
ORDERED BY #pOrden