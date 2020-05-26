 with semana as (
 SELECT to_number(to_char(to_date(r.fecha_inicio,'MM/DD/YYYY'),'WW')) week_num , COUNT(r.propiedad) AS res, o.id as id
 FROM A_RESERVA r
 INNER JOIN A_PROPIEDAD p ON (p.id = r.propiedad) 
 INNER JOIN A_OPERADOR o ON (o.id = p.operador)
 group by to_number(to_char(to_date(r.fecha_inicio,'MM/DD/YYYY'),'WW')),o.id 
 )
 
 SELECT MIN(s.res), o.id, o.numero_rnt, o.vencimiento_rnt, o.registro_super_turismo, o.vencimiento_registro_st, o.categoria, o.direccion, o.hora_apertura,o.hora_cierre
 ,o.tiempo_minimo, o.ganancia_anio_actual, o.ganancia_anio_corrido, o.habitacion, o.apartamento
 FROM A_OPERADOR o
 INNER JOIN semana s ON (s.id = o.id) group by o.id, o.numero_rnt, o.vencimiento_rnt, o.registro_super_turismo, o.vencimiento_registro_st, 
o.categoria, o.direccion, o.hora_apertura, o.hora_cierre, o.tiempo_minimo, 
o.ganancia_anio_actual, o.ganancia_anio_corrido, o.habitacion, o.apartamento
  