package io.github.tedtedted.craigslist.model;

public enum ForSaleCategory implements CategoryCode {
  ALL_FOR_SALE("sss", "for sale - all"),
  ANTIQUES("ata", "antiques"),
  APPLIANCES("ppa", "appliances"),
  ARTS_CRAFTS("ard", "arts & crafts"),
  ATVS_UTVS_SNOWMOBILES("sno", "atvs, utvs, snowmobiles"),
  AUTO_PARTS("pta", "auto parts"),
  AUTO_WHEELS_TIRES("wto", "auto wheels & tires"),
  AVIATION("ava", "aviation"),
  BABY_KID_STUFF("baa", "baby & kid stuff"),
  BARTER("bar", "barter"),
  BICYCLE_PARTS("bip", "bicycle parts"),
  BICYCLES("bia", "bicycles"),
  BOAT_PARTS("bpa", "boat parts & accessories"),
  BOATS("boo", "boats"),
  BOOKS_MAGAZINES("bka", "books & magazines"),
  BUSINESS("bfa", "business / commercial"),
  CARS_TRUCKS("cta", "cars & trucks"),
  CDS_DVDS_VHS("ema", "cds / dvds / vhs"),
  CELL_PHONES("moa", "cell phones"),
  CLOTHING_ACCESSORIES("cla", "clothing & accessories"),
  COLLECTIBLES("cba", "collectibles"),
  COMPUTER_PARTS("syp", "computer parts"),
  COMPUTERS("sya", "computers"),
  ELECTRONICS("ela", "electronics"),
  FARM_GARDEN("gra", "farm & garden"),
  FREE("zip", "free stuff"),
  FURNITURE("fua", "furniture"),
  GARAGE_SALES("gms", "garage & moving sales"),
  GENERAL_FOR_SALE("foa", "general for sale"),
  HEAVY_EQUIPMENT("hva", "heavy equipment"),
  HOUSEHOLD("haa", "household items"),
  JEWELRY("jwa", "jewelry"),
  MATERIALS("maa", "materials"),
  MOTORCYCLE_PARTS("mpa", "motorcycle parts"),
  MOTORCYCLES("mca", "motorcycles"),
  MUSIC_INSTRUMENTS("msa", "musical instruments"),
  PHOTO_VIDEO("pha", "photo / video"),
  RVS("rva", "rvs"),
  SPORTING_GOODS("sga", "sporting goods"),
  TICKETS("tia", "tickets"),
  TOOLS("tla", "tools"),
  TOYS_GAMES("taa", "toys & games"),
  TRAILERS("tra", "trailers"),
  VIDEO_GAMING("vga", "video gaming"),
  WANTED("waa", "wanted");

  private final String code;
  private final String displayName;

  ForSaleCategory(String code, String displayName) {
    this.code = code;
    this.displayName = displayName;
  }

  @Override
  public String code() {
    return code;
  }

  @Override
  public String displayName() {
    return displayName;
  }
}
