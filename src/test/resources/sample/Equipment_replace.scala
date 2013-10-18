package code.model

import net.liftweb.common._

import net.liftweb.mapper._

import com.geishatokyo.zoffylib.database.dao._

// ##hold import
// ##end


class Equipment private  extends Mapper[Equipment] with IdPK {
  def getSingleton = Equipment
  lazy val cache = new com.geishatokyo.zoffylib.database.dao.CacheMethod {
    def delete: Boolean = true
    def exists: Boolean = true
    def keyString: String = ""
  }
  object bookId extends MappedLong(this) {
    ()
  }
  object category extends MappedInt(this) {
    ()
  }
  object equipType extends MappedInt(this) {
    ()
  }
  object weaponType extends MappedInt(this) {
    ()
  }
  object rarity extends MappedInt(this) {
    ()
  }
  object zIndex extends MappedInt(this) {
    ()
  }
  object name extends MappedString(this, 256) {
    ()
  }
  object weight extends MappedInt(this) {
    ()
  }
  object fixedSkillId extends MappedLong(this) {
    ()
  }
  object skillPoint extends MappedInt(this) {
    ()
  }
  object magicId extends MappedLong(this) {
    ()
  }
  object maxLv extends MappedInt(this) {
    ()
  }
  object maxRank extends MappedInt(this) {
    ()
  }
  object noHair extends MappedBoolean(this) {
    ()
  }
  object localCoin extends MappedLong(this) {
    ()
  }
  object effectName extends MappedString(this, 256) {
    ()
  }
  object evolutionBeginDate extends MappedDateTime(this) {
    ()
  }
  object evolutionEndDate extends MappedDateTime(this) {
    ()
  }
  // ##hold classDef
  // ##end
  
}

object Equipment extends Equipment with MetaMapper[Equipment] {
  val group = "装備"
  override def displayName = "装備"
  override def fieldsForList = id :: super.fieldsForList
  // ##hold objectDef
  // ##end
  
}