select MSISDN, b.description 
                    from RPETEPIDAT.raid_t_pi_alarms a, RPETEPIDAT.RAID_R_PI_ALARM_TYPE b
                    where a.alarm_type = b.alarm_type
                    and load_proc_id = ?
					and a.alarm_type = ?
