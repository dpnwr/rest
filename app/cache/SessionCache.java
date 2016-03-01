package cache;

import java.io.IOException;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.owera.common.db.ConnectionProperties;
import com.owera.common.db.NoAvailableConnectionException;
import com.owera.common.util.Cache;
import com.owera.common.util.CacheValue;
import com.owera.xaps.dbi.*;
import com.owera.xaps.dbi.report.PeriodType;
import com.owera.xaps.dbi.report.RecordHardware;
import com.owera.xaps.dbi.report.RecordSyslog;
import com.owera.xaps.dbi.report.RecordVoip;
import com.owera.xaps.dbi.report.Report;
import com.owera.xaps.dbi.report.ReportConverter;
import com.owera.xaps.dbi.report.ReportHardwareGenerator;
import com.owera.xaps.dbi.report.ReportSyslogGenerator;
import com.owera.xaps.dbi.report.ReportVoipGenerator;
import service.XAPSLoader;


/**
 * The Session Store for xAPS Web.
 * Uses session id as unique identifier.
 *
 * @author Jarl Andre Hubenthal
 */
public class SessionCache {

    private static Cache cache = new Cache();

    private static final long UNIT_SESSION_TIMEOUT = 15 * 1000; // 15 seconds

    private static final long SYSLOG_EXPORT_TIMEOUT = 60 * 60 * 1000; // 60 seconds

    private static final SimpleDateFormat SYSLOG_RESULT_CACHE_KEY_FORMAT = new SimpleDateFormat("yyyyMMddHH");

    // TODO must remember to stop DBI thread before setting new Cache
    public static void reset() {
        cache = new Cache();
    }

    private static String key(String sessionId, String keypart) {
        return sessionId + keypart;
    }

    public static void putDBI(String sessionId, DBI xapsCache, int lifeTimeSec) {
        if (xapsCache == null) {
            cache.remove(key(sessionId, "dbi"));
        } else {
            cache.put(key(sessionId, "dbi"), new CacheValue(xapsCache, Cache.SESSION, (lifeTimeSec * 1000)));
        }
    }

    public static DBI getDBI(String sessionId) {
        if (cache.get(key(sessionId, "dbi")) != null)
            return (DBI) cache.get(key(sessionId, "dbi")).getObject();
        return null;
    }

    public static XAPS getXAPS(String sessionId) {
        if (cache.get(key(sessionId, "dbi")) != null)
            return ((DBI) cache.get(key(sessionId, "dbi")).getObject()).getXaps();
        return null;
    }

    public static void putUnit(String sessionId, Unit unit) {
        if (unit != null) {
            String key = key(sessionId, unit.getId());
            cache.put(key, new CacheValue(unit, Cache.SESSION, UNIT_SESSION_TIMEOUT));
        }
    }

    public static Unit getUnit(String sessionId, String unitId) {
        if (cache.get(key(sessionId, unitId)) != null)
            return (Unit) cache.get(key(sessionId, unitId)).getObject();
        return null;
    }

    public static SessionData getSessionData(String sessionId) {
        CacheValue cv = cache.get(key(sessionId, "sessionData"));
        if (cv == null) {
            SessionData sessionData = new SessionData();
            cache.put(key(sessionId, "sessionData"), new CacheValue(sessionData, Cache.SESSION, Long.MAX_VALUE));
            return sessionData;
        }
        return (SessionData) cv.getObject();
    }

    public static void removeSessionData(String sessionId) {
        cache.remove(key(sessionId, "sessionData"));
    }

    public static void putSyslogConnectionProperties(ConnectionProperties props) {
        if (props == null)
            cache.remove(key("nokey", "syslogprops"));
        else
            cache.put(key("nokey", "syslogprops"), new CacheValue(props, Cache.SESSION, Long.MAX_VALUE));
    }

    public static ConnectionProperties getSyslogConnectionProperties() {
        CacheValue cv = cache.get(key("nokey", "syslogprops"));
        if (cv == null)
            return null;
        return (ConnectionProperties) cv.getObject();
    }

    public static void putXAPSConnectionProperties(ConnectionProperties props) {
        if (props == null)
            cache.remove(key("nokey", "xapsprops"));
        else
            cache.put(key("nokey", "xapsprops"), new CacheValue(props, Cache.SESSION, Long.MAX_VALUE));
    }

    public static ConnectionProperties getXAPSConnectionProperties() {
        CacheValue cv = cache.get(key("nokey", "xapsprops"));
        if (cv == null)
            return null;
        return (ConnectionProperties) cv.getObject();
    }

    public static void putSyslogEntries(String sessionId, List<SyslogEntry> entries) {
        if (entries == null)
            cache.remove(key(sessionId, "syslogresults"));
        else
            cache.put(key(sessionId, "syslogresults"), new CacheValue(entries, Cache.SESSION, SYSLOG_EXPORT_TIMEOUT));
    }

    @SuppressWarnings("unchecked")
    public static List<SyslogEntry> getSyslogEntries(String sessionId) {
        CacheValue cv = cache.get(key(sessionId, "syslogresults"));
        if (cv == null)
            return null;
        List<SyslogEntry> object = (List<SyslogEntry>) cv.getObject();
        return object;
    }

//	@SuppressWarnings("unchecked")
//	public static Report<RecordVoip> getVoipReport(String sessionId,String unitId, Date fromDate, Date toDate) throws SQLException, IOException {
//		String key = getRangeKey(unitId,"voipcalls",fromDate,toDate);
//
//		CacheValue cv = cache.get(key);
//		if(cv == null){
//			ReportVoipGenerator rg = new ReportVoipGenerator(getSyslogConnectionProperties(sessionId), getXAPSConnectionProperties(sessionId), XAPSLoader.getXAPS(sessionId), null, XAPSLoader.getIdentity(sessionId));
//			Report<RecordVoip> value = rg.generateFromSyslog(fromDate, toDate, unitId);
//			cv = new CacheValue(value,Cache.ABSOLUTE,SYSLOG_RESULT_TIMEOUT);
//			cache.put(key, cv);
//		}
//
//		return (Report<RecordVoip>) cv.getObject();
//	}

    private static String getRangeKey(String unit, String descriptor, Date from, Date to) {
        return key(unit, descriptor + SYSLOG_RESULT_CACHE_KEY_FORMAT.format(from) + SYSLOG_RESULT_CACHE_KEY_FORMAT.format(to));
    }

    private static String getSyslogRangeKey(String unit, String descriptor, Date from, Date to, String syslogFilter) {
        return key(unit, descriptor + SYSLOG_RESULT_CACHE_KEY_FORMAT.format(from) + SYSLOG_RESULT_CACHE_KEY_FORMAT.format(to) + syslogFilter);
    }

    public static Report<RecordVoip> convertVoipReport(Report<RecordVoip> report, PeriodType type) {
        if (type != null && report.getPeriodType().getTypeInt() != type.getTypeInt())
            report = ReportConverter.convertVoipReport(report, type);
        return report;
    }

//	@SuppressWarnings("unchecked")
//	public static Report<RecordHardware> getHardwareReport(String sessionId,String unitId, Date fromDate, Date toDate) throws NoAvailableConnectionException, SQLException, IOException {
//		String key = getRangeKey(unitId,"hardwarereport",fromDate,toDate);
//
//		CacheValue cv = cache.get(key);
//		if(cv == null){
//			ReportHardwareGenerator rg = new ReportHardwareGenerator(getSyslogConnectionProperties(sessionId), getXAPSConnectionProperties(sessionId),XAPSLoader.getXAPS(sessionId), null,XAPSLoader.getIdentity(sessionId));
//			Report<RecordHardware> value = rg.generateFromSyslog(fromDate, toDate, unitId);
//			cv = new CacheValue(value,Cache.ABSOLUTE,SYSLOG_RESULT_TIMEOUT);
//			cache.put(key, cv);
//		}
//
//		return (Report<RecordHardware>) cv.getObject();
//	}

    public static Report<RecordHardware> convertHardwareReport(Report<RecordHardware> report, PeriodType type) {
        if (type != null && report.getPeriodType().getTypeInt() != type.getTypeInt())
            report = ReportConverter.convertHardwareReport(report, type);
        return report;
    }

//	@SuppressWarnings("unchecked")
//	public static Report<RecordSyslog> getSyslogReport(String sessionId,String unitId, Date fromDate, Date toDate, String syslogFilter) throws NoAvailableConnectionException, SQLException, IOException, ParseException {
//		String key = getSyslogRangeKey(unitId,"syslogreport",fromDate,toDate,syslogFilter);
//
//		CacheValue cv = cache.get(key);
//		if(cv == null){
//			SyslogFilter filter = new SyslogFilter();
//			filter.setMessage(syslogFilter);
//			ReportSyslogGenerator rg = new ReportSyslogGenerator(getSyslogConnectionProperties(sessionId), getXAPSConnectionProperties(sessionId),XAPSLoader.getXAPS(sessionId), null,XAPSLoader.getIdentity(sessionId));
//			rg.setSyslogFilter(filter);
//			Report<RecordSyslog> value = rg.generateFromSyslog(fromDate, toDate, unitId);
//			cv = new CacheValue(value,Cache.ABSOLUTE,SYSLOG_RESULT_TIMEOUT);
//			cache.put(key, cv);
//		}
//
//		return (Report<RecordSyslog>) cv.getObject();
//	}

    public static void deleteSyslogReport(String sessionId, String unitId, Date fromDate, Date toDate) {
        cache.remove(getRangeKey(unitId, sessionId, fromDate, toDate));
    }

    public static Report<RecordSyslog> convertSyslogReport(Report<RecordSyslog> report, PeriodType type) {
        if (type != null && report.getPeriodType().getTypeInt() != type.getTypeInt())
            report = ReportConverter.convertSyslogReport(report, type);
        return report;
    }

    public static Cache getCache() {
        return cache;
    }
}