with res as ( SELECT count(*) AS mes , c.id as id, count(c.reservas) as reservas 
FROM A_CLIENTE c
INNER JOIN A_RESERVA r ON (c.reservas = r.id) 
INNER JOIN A_PROPIEDAD p ON (p.id = r.propiedad)
WHERE to_char(r.fecha_inicio,'MM')- to_char((  
SELECT r2.fecha_inicio 
FROM A_CLIENTE c2
INNER JOIN A_RESERVA r2 ON (c2.reservas = r2.id)
INNER JOIN A_PROPIEDAD p2 ON (p2.id = r2.propiedad)
WHERE c.id = c2.id)) = -1  
group by c.id
)
SELECT  c.*, p.precio , h.tipo, re.mes 
FROM A_CLIENTE c 
INNER JOIN A_RESERVA r ON (c.reservas = r.id) 
INNER JOIN A_PROPIEDAD p ON (p.id = r.propiedad)
INNER JOIN A_HABITACION h ON (p.id = h.id) 
INNER JOIN A_APARTAMENTO ap ON (p.id = ap.id)
INNER JOIN res re ON (re.id = c.id) 
WHERE p.precio > 150 OR h.tipo = 3 OR re.mes = re.reservas
    