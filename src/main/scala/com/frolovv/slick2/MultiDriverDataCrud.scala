package com.frolovv.slick2

import scala.slick.driver.JdbcDriver
import scala.util.Try

/**
 * Created by valeryf on 5/12/15
 */
class MultiDriverDataCrud(val driver: JdbcDriver, val db: JdbcDriver.simple.Database) extends DataCrud {

  import driver.simple._

  class DataTable(tag: Tag) extends Table[(Long, String)](tag, "table") {

    def id: Column[Long] = column[Long]("id", O.PrimaryKey)

    def value: Column[String] = column[String]("value")

    def * = (id, value)
  }

  private val table = TableQuery[DataTable]

  private def reset = db.withSession { implicit session =>
    Try(table.ddl.drop)
    Try(table.ddl.create)
  }

  override def insert(data: Data): Unit = db.withSession { implicit session =>
    table +=(data.id, data.value)
  }

  override def update(data: Data): Unit = db.withSession { implicit session =>
    table
      .filter(row => row.id === data.id)
      .map(row => row.value)
      .update(data.value)
  }

  override def delete(data: Data): Unit = db.withSession { implicit session =>
    table
      .filter(row => row.id === data.id)
      .delete
  }

  override def getById(lookupId: Long): Option[Data] = db.withSession { implicit session =>
    table
      .filter(row => row.id === lookupId)
      .map(row => (row.id, row.value))
      .firstOption
      .map { case (id, value) => Data(id, value) }
  }
}

object MultiDriverDataCrud {
  def h2(): MultiDriverDataCrud = {
    val driver = scala.slick.driver.H2Driver
    val database = driver.simple.Database

    val db = database.forURL("jdbc:h2:mem:test;DB_CLOSE_DELAY=-1;TRACE_LEVEL_FILE=0;TRACE_LEVEL_SYSTEM_OUT=0", driver = "org.h2.Driver")

    val crud = new MultiDriverDataCrud(driver, db)
    crud.reset
    crud
  }

  import javax.sql.DataSource

  def mysql(dataSource: DataSource): MultiDriverDataCrud = {
    val driver = scala.slick.driver.MySQLDriver
    val db = driver.simple.Database.forDataSource(dataSource)

    new MultiDriverDataCrud(driver, db)
  }

  def sqlite(): MultiDriverDataCrud = {
    val driver = scala.slick.driver.H2Driver
    val database = driver.simple.Database

    val db = database.forURL("jdbc:sqlite::memory:;DB_CLOSE_DELAY=-1;", driver = "org.sqlite.JDBC")

    val crud = new MultiDriverDataCrud(driver, db)
    crud.reset
    crud
  }
}


