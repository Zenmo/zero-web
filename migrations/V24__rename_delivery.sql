ALTER TABLE grid_connection
    RENAME COLUMN annual_district_heating_demand_gj TO annual_district_heating_delivery_gj;

ALTER TABLE grid_connection
    RENAME COLUMN annual_electricity_demand_kwh TO annual_electricity_delivery_kwh;

ALTER TABLE grid_connection
    RENAME COLUMN annual_electricity_production_kwh TO annual_electricity_feed_in_kwh;

ALTER TABLE grid_connection
    RENAME COLUMN grootverbruik_contracted_demand_capacity_kw TO grootverbruik_contracted_delivery_capacity_kw;

ALTER TABLE grid_connection
    RENAME COLUMN grootverbruik_contracted_supply_capacity_kw TO grootverbruik_contracted_feed_in_capacity_kw;

ALTER TABLE grid_connection
    RENAME COLUMN natural_gas_annual_demand_m3 TO natural_gas_annual_delivery_m3;

CREATE TYPE kleinverbruikorgrootverbruik AS ENUM ('KLEINVERBRUIK', 'GROOTVERBRUIK');
ALTER TABLE grid_connection
    ADD kleinverbruik_or_grootverbruik kleinverbruikorgrootverbruik;

UPDATE grid_connection
    SET kleinverbruik_or_grootverbruik = 'KLEINVERBRUIK'
    WHERE kleinverbuik_electricity_connection_capacity IS NOT NULL;

UPDATE grid_connection
    SET kleinverbruik_or_grootverbruik = 'GROOTVERBRUIK'
    WHERE grootverbruik_contracted_delivery_capacity_kw IS NOT NULL
    OR grootverbruik_contracted_feed_in_capacity_kw IS NOT NULL;
