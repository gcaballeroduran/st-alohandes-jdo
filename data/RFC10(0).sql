-- En caso de  que la reserva sea en un 
SELECT count(c.reservas) as res,* 
FROM A_Cliente c NATURAL INNER JOIN A_PROPIEDAD p, A_HABITACION h, A_APARTAMENTO a, 
A_RESERVAHABITACION rh, A_RESESRVAAPARTAMENTO ra

WHERE p.id = #pIdProp AND r.FechaInicio BETWEEN TO_DATE(#pFI) AND TO_DATE (#pFF) AND res>0
ORDERED BY #pOrden