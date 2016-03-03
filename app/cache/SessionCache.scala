package cache

import java.text.SimpleDateFormat
import java.util.Date
import com.owera.common.db.ConnectionProperties
import com.owera.common.util.Cache
import com.owera.common.util.CacheValue
import com.owera.xaps.dbi._
import com.owera.xaps.dbi.report.PeriodType
import com.owera.xaps.dbi.report.RecordHardware
import com.owera.xaps.dbi.report.RecordSyslog
import com.owera.xaps.dbi.report.RecordVoip
import com.owera.xaps.dbi.report.Report
import com.owera.xaps.dbi.report.ReportConverter
import dto.WebUser

object SessionCache {
  private var cache = new Cache
  private val UNIT_SESSION_TIMEOUT: Long = 15 * 1000
  private val SYSLOG_EXPORT_TIMEOUT: Long = 60 * 60 * 1000
  private val SYSLOG_RESULT_CACHE_KEY_FORMAT = new SimpleDateFormat("yyyyMMddHH")

  private def key(sessionId: String, keypart: String) =
    sessionId + keypart
  private def getRangeKey(unit: String, descriptor: String, from: Date, to: Date) =
    key(unit, descriptor + SYSLOG_RESULT_CACHE_KEY_FORMAT.format(from) + SYSLOG_RESULT_CACHE_KEY_FORMAT.format(to))
  private def getSyslogRangeKey(unit: String, descriptor: String, from: Date, to: Date, syslogFilter: String) =
    key(unit, descriptor + SYSLOG_RESULT_CACHE_KEY_FORMAT.format(from) + SYSLOG_RESULT_CACHE_KEY_FORMAT.format(to) + syslogFilter)

  def reset() = cache = new Cache

  def putDBI(sessionId: String, xapsCache: DBI, lifeTimeSec: Int) =
    Option(xapsCache) match {
      case Some(_) =>
        cache.put(key(sessionId, "dbi"), new CacheValue(xapsCache, Cache.SESSION, lifeTimeSec * 1000))
      case _ =>
        cache.remove(key(sessionId, "dbi"))
    }

  def getDBI(sessionId: String) =
    Option(cache.get(key(sessionId, "dbi")))
      .map(_.getObject.asInstanceOf[DBI])
      .orNull

  def getXAPS(sessionId: String) =
    Option(getDBI(sessionId))
      .map(_.getXaps)
      .getOrElse(throw new IllegalStateException("No DBI is cached for this sessionId: " + sessionId))

  def putUnit(sessionId: String, unit: Unit) =
    Option(unit) match {
      case Some(_) =>
        cache.put(key(sessionId, unit.getId), new CacheValue(unit, Cache.SESSION, UNIT_SESSION_TIMEOUT))
      case _ =>
    }

  def getUnit(sessionId: String, unitId: String) =
    Option(cache.get(key(sessionId, unitId)))
      .map(_.getObject.asInstanceOf[Unit])
      .orNull

  def putUser(sessionId: String, user: WebUser) = {
    val cacheKey = key(sessionId, "sessionData")
    Option(cache.get(cacheKey)) match {
      case Some(value) =>
        val sessionData = value.getObject.asInstanceOf[SessionData]
        value.setObject(sessionData.copy(user = user))
        cache.put(cacheKey, value)
      case _ =>
        cache.put(cacheKey, new CacheValue(new SessionData(user), Cache.SESSION, java.lang.Long.MAX_VALUE))
    }
  }

  def getSessionData(sessionId: String): SessionData =
    Option(cache.get(key(sessionId, "sessionData")))
      .map(_.getObject.asInstanceOf[SessionData])
      .getOrElse {
        val sessionData = new SessionData(null)
        cache.put(key(sessionId, "sessionData"), new CacheValue(sessionData, Cache.SESSION, java.lang.Long.MAX_VALUE))
        sessionData
      }

  def removeSessionData(sessionId: String) =
    cache.remove(key(sessionId, "sessionData"))

  def putSyslogConnectionProperties(props: ConnectionProperties) =
    Option(props) match {
      case Some(_) =>
        cache.put(key("nokey", "syslogprops"), new CacheValue(props, Cache.SESSION, java.lang.Long.MAX_VALUE))
      case _ =>
        cache.remove(key("nokey", "syslogprops"))
    }

  def getSyslogConnectionProperties =
    Option(cache.get(key("nokey", "syslogprops")))
      .map(_.getObject.asInstanceOf[ConnectionProperties])
      .orNull

  def putXAPSConnectionProperties(props: ConnectionProperties) =
    Option(props) match {
      case Some(_) =>
        cache.put(key("nokey", "xapsprops"), new CacheValue(props, Cache.SESSION, java.lang.Long.MAX_VALUE))
      case _ =>
        cache.remove(key("nokey", "xapsprops"))
    }

  def getXAPSConnectionProperties =
    Option(cache.get(key("nokey", "xapsprops")))
      .map(_.getObject.asInstanceOf[ConnectionProperties])
      .orNull

  def putSyslogEntries(sessionId: String, entries: java.util.List[SyslogEntry]) =
    Option(entries) match {
      case Some(_) =>
        cache.put(key(sessionId, "syslogresults"), new CacheValue(entries, Cache.SESSION, SYSLOG_EXPORT_TIMEOUT))
      case _ =>
        cache.remove(key(sessionId, "syslogresults"))
    }

  def getSyslogEntries(sessionId: String) =
    Option(cache.get(key(sessionId, "syslogresults")))
      .map(_.getObject.asInstanceOf[java.util.List[SyslogEntry]])
      .orNull

  def convertVoipReport(report: Report[RecordVoip], pType: Option[PeriodType]) =
    pType.filter(_.getTypeInt != report.getPeriodType.getTypeInt)
      .map(pt => ReportConverter.convertVoipReport(report, pt))
      .getOrElse(report)

  def convertHardwareReport(report: Report[RecordHardware], pType: Option[PeriodType]) =
    pType.filter(_.getTypeInt != report.getPeriodType.getTypeInt)
      .map(pt => ReportConverter.convertHardwareReport(report, pt))
      .getOrElse(report)

  def deleteSyslogReport(sessionId: String, unitId: String, fromDate: Date, toDate: Date) =
    cache.remove(getRangeKey(unitId, sessionId, fromDate, toDate))

  def t = convertSyslogReport(null, null)
  def convertSyslogReport(report: Report[RecordSyslog], periodType: Option[PeriodType]) =
    periodType.filter(_.getTypeInt != report.getPeriodType.getTypeInt)
      .map(pt => ReportConverter.convertSyslogReport(report, pt))
      .getOrElse(report)

  def getCache = cache
}