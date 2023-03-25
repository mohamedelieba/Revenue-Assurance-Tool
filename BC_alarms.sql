SELECT msisdn,
       CASE 	
         WHEN ALARM_TYPE= 'BSCS-SS-1vsCUDB-SS-2' THEN 	
           'The subscriber status does not match between the BSCS and the CUDB' 	
         WHEN ALARM_TYPE= 'BSCS-IMSI-1vsCUDB-IMSI-2' THEN 	
           'The pair IMSI does not match between the BSCS and the CUDB' 	
         WHEN ALARM_TYPE= 'BSCS-CF-1vsCUDB-CF-2' THEN 	
           'The CAMEL flag does not macth between the BSCS, and the CUDB' 	
         WHEN ALARM_TYPE= 'BSCS-ST-1vsSDP-ST-2' THEN 	
           'The Subscriber Type does not match between the BSCS and the SDP' 	
         WHEN ALARM_TYPE= 'BSCS-SS-1vsSDP-SS-2' THEN 	
           'The Subscriber Status does not match between the BSCS and the SDP' 	
         WHEN ALARM_TYPE= 'BSCS-SC-1vsSDP-SC-2' THEN 	
           'The Service Class does not match between the BSCS and the SDP' 	
         WHEN ALARM_TYPE= 'BSCS-BC-1vsSDP-BC-2' THEN 	
           'The Billing Cycle does not match between the BSCS and the SDP' 	
         WHEN ALARM_TYPE= 'BSCS-APN-1vsCUDB-APN-2' THEN 	
           'The APN does not macth between the BSCS, and the CUDB' 	
       END AS ALARM_DESCRIPTION 	
FROM RPETEPIAPP.raid_t_pi_alarms
WHERE LOAD_PROC_ID = ?
AND ALARM_TYPE IN (
  'BSCS-SS-1vsCUDB-SS-2',
  'BSCS-IMSI-1vsCUDB-IMSI-2',
  'BSCS-CF-1vsCUDB-CF-2',
  'BSCS-ST-1vsSDP-ST-2',
  'BSCS-SS-1vsSDP-SS-2',
  'BSCS-SC-1vsSDP-SC-2',
  'BSCS-BC-1vsSDP-BC-2',
  'BSCS-APN-1vsCUDB-APN-2'
)
