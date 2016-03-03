package service

import java.sql.SQLException
import cache.SessionCache
import com.owera.xaps.dbi._
import dto.WebUser
import play.{Logger, Play}
import com.google.inject.Singleton
import com.owera.common.db.ConnectionProperties
import play.api.Application


object XAPSLoader {

  def getConnectionProperties = {
    val url = Play.application.configuration.getString("xaps.db.url", null)
    if (url == null) {
      throw new IllegalArgumentException("No xaps.db.url in application properties")
    }
    val props = new ConnectionProperties
    try {
      props.setUrl(url.substring(url.indexOf("@") + 1))
      props.setUser(url.substring(0, url.indexOf("/")))
      props.setPassword(url.substring(url.indexOf("/") + 1, url.indexOf("@")))
    } catch {
      case seoobe: StringIndexOutOfBoundsException => {
        throw new IllegalArgumentException(url + " is not on a correct database-config-format (<user>/<password>@<jdbc-url>")
      }
    }
    if (props.getUrl.contains("mysql")) {
      props.setDriver("com.mysql.jdbc.Driver")
    } else {
      throw new IllegalArgumentException("The url is not pointing to a MySQL database")
    }
    props
  }

  def getDBI(sessionId: String): DBI = {
    val currentDBI = SessionCache.getDBI(sessionId)
    if (currentDBI == null || currentDBI.isFinished) {
      val cp = SessionCache.getXAPSConnectionProperties
      if (cp == null) return null
      val ident = getIdentity(sessionId)
      val syscp = SessionCache.getSyslogConnectionProperties
      if (syscp == null) return null
      try {
        val sessionTimeoutSecs = getSessionTimeout * 60
        val syslog: Syslog = new Syslog(syscp, ident)
        val dbi = new DBI(sessionTimeoutSecs, cp, syslog)
        SessionCache.putDBI(sessionId, dbi, sessionTimeoutSecs)
        dbi
      } catch {
        case sql: SQLException => throw sql
        case run: RuntimeException => throw run
      }
    }
    currentDBI
  }

  def getXAPS(sessionId: String): XAPS =
    Option(getDBI(sessionId))
      .map(_.getXaps)
      .orNull

  def getSessionTimeout = Play.application.configuration.getInt("xaps.session.timeout")

  def getIdentity(sessionId: String) = {
    var user: User = SessionCache.getSessionData(sessionId).user
    if (user == null) user = getDefaultUser
    new Identity(SyslogConstants.FACILITY_WEB, classOf[Application].getPackage.getImplementationVersion, user)
  }

  private def getDefaultUser =
    new User("anonymous", null, null, false, new Users(SessionCache.getXAPSConnectionProperties))

  def getXAPSUnit(sessionId: String): XAPSUnit = {
    val cp: ConnectionProperties = SessionCache.getXAPSConnectionProperties
    if (cp == null) return null
    val xaps: XAPS = getDBI(sessionId).getXaps
    if (xaps == null) return null
    val ident: Identity = getIdentity(sessionId)
    val syscp: ConnectionProperties = SessionCache.getSyslogConnectionProperties
    if (syscp == null) return null
    val syslog: Syslog = new Syslog(syscp, ident)
    new XAPSUnit(cp, xaps, syslog)
  }
}