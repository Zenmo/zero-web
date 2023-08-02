export type Productsoort = 'ELK' | 'GAS'

export type SoortGasaansluiting =
    "G4" |
    "G6" |
    "G10" |
    "G16" |
    "G25"

export type SoortElektriciteitsaansluiting =
    "1x25" |
    "1x35" |
    "1x50" |
    "3x25" |
    "3x35" |
    "3x50" |
    "3x63" |
    "3x80"

// Liander, Enexis and Stedin all publish in a similar format.
export interface PostcodeKleinverbruik  {
    netbeheerder: string
    netgebied: string
    straatnaam: string
    postcodeVan: string // 1234AB
    postcodeTot: string // In most cases this is the same as postcodeVan
    woonplaats: string
    landcode: "NL"
    productsoort: Productsoort
    verbruikssegment: "KVB"
    // Het aantal aansluitingen in het betreffende postcodegebied voor de betreffende energiesoort
    aansluitingenAantal: number
    // Percentage van de aansluitingen dat netto elektriciteits- of
    // gasverbruik heeft. Dit percentage wordt lager naarmate er
    // meer teruglevering plaatsvindt (b.v. vanwege
    // zonnepanelen)
    leveringsrichtingPerc: number
    // Het percentage van de aansluitingen dat in bedrijf is
    fysiekeStatusPerc: number
    soortAansluitingPerc: number
    soortAansluiting: SoortGasaansluiting | SoortElektriciteitsaansluiting
    sjvGemiddeld: number // sjv = standaardjaarverbruik in m3 of kWh
    sjvLaagTariefPerc: number
    slimmeMeterPerc: number
}

export interface PostcodeGasKleinverbruik extends PostcodeKleinverbruik {
    productsoort: "GAS"
    soortAansluiting: SoortGasaansluiting
}

export interface PostcodeElektriciteitKleinverbruik extends PostcodeKleinverbruik {
    productsoort: "ELK"
    soortAansluiting: SoortElektriciteitsaansluiting
}
