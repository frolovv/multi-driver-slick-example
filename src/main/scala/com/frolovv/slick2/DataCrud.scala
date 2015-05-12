package com.frolovv.slick2

/**
 * Created by valeryf on 5/12/15
 */
case class Data(id: Long, value: String)

trait DataCrud {

  def insert(data: Data): Unit

  def update(data: Data): Unit

  def delete(data: Data): Unit

  def getById(id: Long): Option[Data]
}

