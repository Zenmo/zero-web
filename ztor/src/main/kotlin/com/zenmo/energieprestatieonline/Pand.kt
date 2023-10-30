package com.zenmo.energieprestatieonline

import org.jetbrains.exposed.sql.Table
import org.jetbrains.exposed.sql.kotlin.datetime.date

// This is imported data from EP (Energieprestatie) Online.
// It has almost 5,4 million records.
object RawPandTable: Table("energielabel_pand") {
    val opnamedatum = date("opnamedatum")
    val opnametype = varchar("opnametype", 1000).nullable()
    val status = varchar("status", 1000).nullable()
    val berekeningstype = varchar("berekeningstype", 1000)
    val energieindex = float("energieindex").nullable()
    val energieklasse = varchar("energieklasse", 1000).nullable()
    val energielabel_is_prive = varchar("energielabel_is_prive", 1000).nullable()
    val is_op_basis_van_referentie_gebouw = bool("is_op_basis_van_referentie_gebouw")
    val gebouwklasse = varchar("gebouwklasse", 1000)
    val meting_geldig_tot = date("meting_geldig_tot")
    val registratiedatum = date("registratiedatum")
    val postcode = varchar("postcode", 1000).nullable()
    val huisnummer = uinteger("huisnummer").nullable()
    val huisletter = varchar("huisletter", 1000).nullable()
    val huisnummertoevoeging = varchar("huisnummertoevoeging", 1000).nullable()
    val detailaanduiding = varchar("detailaanduiding", 1000).nullable()
    val bagverblijfsobjectid = varchar("bagverblijfsobjectid", 1000).nullable()
    val bagligplaatsid = varchar("bagligplaatsid", 1000).nullable()
    val bagstandplaatsid = varchar("bagstandplaatsid", 1000).nullable()
    val bagpandid = varchar("bagpandid", 1000).nullable()
    val gebouwtype = varchar("gebouwtype", 1000).nullable()
    val gebouwsubtype = varchar("gebouwsubtype", 1000).nullable()
    val projectnaam = varchar("projectnaam", 1000).nullable()
    val projectobject = varchar("projectobject", 1000).nullable()
    // It has around 129.000 records with an SBI code (Standaard Bedrijfs Indeling)
    val SBIcode = varchar("SBIcode", 1000).nullable()
    val gebruiksoppervlakte_thermische_zone = float("gebruiksoppervlakte_thermische_zone").nullable()
    val energiebehoefte = float("energiebehoefte").nullable()
    val eis_energiebehoefte = float("eis_energiebehoefte").nullable()
    val primaire_fossiele_energie = float("primaire_fossiele_energie").nullable()
    val eis_primaire_fossiele_energie = float("eis_primaire_fossiele_energie").nullable()
    val primaire_fossiele_energie_EMG_forfaitair = float("primaire_fossiele_energie_EMG_forfaitair").nullable()
    val aandeel_hernieuwbare_energie = float("aandeel_hernieuwbare_energie").nullable()
    val eis_aandeel_hernieuwbare_energie = float("eis_aandeel_hernieuwbare_energie").nullable()
    val aandeel_hernieuwbare_energie_EMG_forfaitair = float("aandeel_hernieuwbare_energie_EMG_forfaitair").nullable()
    val temperatuuroverschrijding = float("temperatuuroverschrijding").nullable()
    val eis_temperatuuroverschrijding = float("eis_temperatuuroverschrijding").nullable()
    val warmtebehoefte = float("warmtebehoefte").nullable()
    val energieindex_met_EMG_forfaitair = float("energieindex_met_EMG_forfaitair").nullable()
}
