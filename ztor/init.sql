CREATE TYPE KleinverbruikElectricityConsumptionProfile AS ENUM ('ONE', 'TWO', 'THREE', 'FOUR');
CREATE TYPE HeatingType AS ENUM ('GAS_BOILER', 'ELECTRIC_HEATPUMP', 'HYBRID_HEATPUMP', 'DISTRICT_HEATING', 'OTHER');
CREATE TYPE PVOrientation AS ENUM ('SOUTH', 'EAST_WEST');
CREATE TYPE BlobPurpose AS ENUM ('NATURAL_GAS_VALUES', 'ELECTRICITY_VALUES', 'ELECTRICITY_AUTHORIZATION');
CREATE TABLE IF NOT EXISTS company_survey (id uuid PRIMARY KEY, created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP NOT NULL, energieke_regio_id BIGINT NULL, project VARCHAR(50) NOT NULL, company_name VARCHAR(50) NOT NULL, person_name VARCHAR(50) NOT NULL, email VARCHAR(50) NOT NULL, CONSTRAINT chk_company_survey_unsigned_energieke_regio_id CHECK (energieke_regio_id BETWEEN 0 AND 4294967295))
CREATE TABLE IF NOT EXISTS address (id uuid PRIMARY KEY, survey_id uuid NOT NULL, street VARCHAR(50) NOT NULL, house_number BIGINT NOT NULL, house_letter VARCHAR(1) NOT NULL, house_number_addition VARCHAR(50) NOT NULL, postal_code VARCHAR(8) NOT NULL, city VARCHAR(50) NOT NULL, CONSTRAINT fk_address_survey_id__id FOREIGN KEY (survey_id) REFERENCES company_survey(id) ON DELETE RESTRICT ON UPDATE RESTRICT, CONSTRAINT chk_address_unsigned_house_number CHECK (house_number BETWEEN 0 AND 4294967295))
CREATE TABLE IF NOT EXISTS company_survey_grid_connection (id uuid PRIMARY KEY, address_id uuid NOT NULL, energy_or_building_management_system_supplier VARCHAR(1000) NOT NULL, main_consumption_process VARCHAR(1000) NOT NULL, consumption_flexibility VARCHAR(1000) NOT NULL, electrification_plans VARCHAR(1000) NOT NULL, survey_feedback VARCHAR(1000) NOT NULL, electricity_ean VARCHAR(18) NOT NULL, annual_electricity_demand_kwh BIGINT NULL, annual_electricity_production_kwh BIGINT NULL, kleinverbuik_electricity_connection_capacity KleinverbruikElectricityConnectionCapacity NULL, kleinverbuik_electricity_consumption_profile KleinverbruikElectricityConsumptionProfile NULL, grootverbruik_contracted_demand_capacity_kw BIGINT NULL, grootverbruik_contracted_supply_capacity_kw BIGINT NULL, has_supply BOOLEAN NULL, pv_installed_kwp BIGINT NULL, pv_orientation PVOrientation NULL, pv_planned BOOLEAN NULL, pv_planned_kwp BIGINT NULL, pv_planned_orientation PVOrientation NULL, pv_planned_year BIGINT NULL, wind_installed_kw REAL NULL, other_supply VARCHAR(1000) NOT NULL, has_natural_gas_connection BOOLEAN NULL, natural_gas_ean VARCHAR(18) NOT NULL, natural_gas_annual_demand_m3 BIGINT NULL, percentage_natural_gas_for_heating BIGINT NULL, heating_types HeatingType ARRAY NOT NULL, combined_gas_boiler_kw REAL NULL, combined_heat_pump_kw REAL NULL, combined_hybrid_heat_pump_electric_kw REAL NULL, annual_district_heating_demand_gj REAL NULL, local_heat_exchange_description VARCHAR(1000) NOT NULL, has_unused_residual_heat BOOLEAN NULL, has_battery BOOLEAN NULL, battery_capacity_kwh REAL NULL, battery_power_kw REAL NULL, battery_schedule VARCHAR(1000) NOT NULL, has_planned_battery BOOLEAN NULL, planned_battery_capacity_kwh REAL NULL, planned_battery_power_kw REAL NULL, planned_battery_schedule VARCHAR(1000) NOT NULL, has_thermal_storage BOOLEAN NULL, has_vehicles BOOLEAN NULL, num_daily_car_commuters BIGINT NULL, num_trucks BIGINT NULL, num_electric_trucks BIGINT NULL, num_truck_charge_points BIGINT NULL, power_per_truck_charge_point_kw REAL NULL, annual_travel_distance_per_truck_km BIGINT NULL, num_planned_electric_trucks BIGINT NULL, num_vans BIGINT NULL, num_electric_vans BIGINT NULL, num_electric_van_charge_points BIGINT NULL, power_per_van_charge_point_kw REAL NULL, annual_travel_distance_per_van_km BIGINT NULL, num_planned_electric_vans BIGINT NULL, num_cars BIGINT NULL, num_electric_cars BIGINT NULL, num_car_charge_points BIGINT NULL, power_per_car_charge_point_kw REAL NULL, annual_travel_distance_per_car_km BIGINT NULL, num_planned_electric_cars BIGINT NULL, CONSTRAINT fk_company_survey_grid_connection_address_id__id FOREIGN KEY (address_id) REFERENCES address(id) ON DELETE RESTRICT ON UPDATE RESTRICT, CONSTRAINT chk_company_survey_grid_connection_unsigned_annual_electricity_ CHECK (annual_electricity_demand_kwh BETWEEN 0 AND 4294967295), CONSTRAINT chk_company_survey_grid_connection_unsigned_annual_electricity_ CHECK (annual_electricity_production_kwh BETWEEN 0 AND 4294967295), CONSTRAINT chk_company_survey_grid_connection_unsigned_grootverbruik_contr CHECK (grootverbruik_contracted_demand_capacity_kw BETWEEN 0 AND 4294967295), CONSTRAINT chk_company_survey_grid_connection_unsigned_grootverbruik_contr CHECK (grootverbruik_contracted_supply_capacity_kw BETWEEN 0 AND 4294967295), CONSTRAINT chk_company_survey_grid_connection_unsigned_pv_installed_kwp CHECK (pv_installed_kwp BETWEEN 0 AND 4294967295), CONSTRAINT chk_company_survey_grid_connection_unsigned_pv_planned_kwp CHECK (pv_planned_kwp BETWEEN 0 AND 4294967295), CONSTRAINT chk_company_survey_grid_connection_unsigned_pv_planned_year CHECK (pv_planned_year BETWEEN 0 AND 4294967295), CONSTRAINT chk_company_survey_grid_connection_unsigned_natural_gas_annual_ CHECK (natural_gas_annual_demand_m3 BETWEEN 0 AND 4294967295), CONSTRAINT chk_company_survey_grid_connection_unsigned_percentage_natural_ CHECK (percentage_natural_gas_for_heating BETWEEN 0 AND 4294967295), CONSTRAINT chk_company_survey_grid_connection_unsigned_num_daily_car_commu CHECK (num_daily_car_commuters BETWEEN 0 AND 4294967295), CONSTRAINT chk_company_survey_grid_connection_unsigned_num_trucks CHECK (num_trucks BETWEEN 0 AND 4294967295), CONSTRAINT chk_company_survey_grid_connection_unsigned_num_electric_trucks CHECK (num_electric_trucks BETWEEN 0 AND 4294967295), CONSTRAINT chk_company_survey_grid_connection_unsigned_num_truck_charge_po CHECK (num_truck_charge_points BETWEEN 0 AND 4294967295), CONSTRAINT chk_company_survey_grid_connection_unsigned_annual_travel_dista CHECK (annual_travel_distance_per_truck_km BETWEEN 0 AND 4294967295), CONSTRAINT chk_company_survey_grid_connection_unsigned_num_planned_electri CHECK (num_planned_electric_trucks BETWEEN 0 AND 4294967295), CONSTRAINT chk_company_survey_grid_connection_unsigned_num_vans CHECK (num_vans BETWEEN 0 AND 4294967295), CONSTRAINT chk_company_survey_grid_connection_unsigned_num_electric_vans CHECK (num_electric_vans BETWEEN 0 AND 4294967295), CONSTRAINT chk_company_survey_grid_connection_unsigned_num_electric_van_ch CHECK (num_electric_van_charge_points BETWEEN 0 AND 4294967295), CONSTRAINT chk_company_survey_grid_connection_unsigned_annual_travel_dista CHECK (annual_travel_distance_per_van_km BETWEEN 0 AND 4294967295), CONSTRAINT chk_company_survey_grid_connection_unsigned_num_planned_electri CHECK (num_planned_electric_vans BETWEEN 0 AND 4294967295), CONSTRAINT chk_company_survey_grid_connection_unsigned_num_cars CHECK (num_cars BETWEEN 0 AND 4294967295), CONSTRAINT chk_company_survey_grid_connection_unsigned_num_electric_cars CHECK (num_electric_cars BETWEEN 0 AND 4294967295), CONSTRAINT chk_company_survey_grid_connection_unsigned_num_car_charge_poin CHECK (num_car_charge_points BETWEEN 0 AND 4294967295), CONSTRAINT chk_company_survey_grid_connection_unsigned_annual_travel_dista CHECK (annual_travel_distance_per_car_km BETWEEN 0 AND 4294967295), CONSTRAINT chk_company_survey_grid_connection_unsigned_num_planned_electri CHECK (num_planned_electric_cars BETWEEN 0 AND 4294967295))
CREATE TABLE IF NOT EXISTS file (grid_connection_id uuid NOT NULL, purpose BlobPurpose NOT NULL, remote_name VARCHAR(1000) PRIMARY KEY, original_name VARCHAR(100) NOT NULL, content_type VARCHAR(100) NULL, "size" INT NOT NULL, CONSTRAINT fk_file_grid_connection_id__id FOREIGN KEY (grid_connection_id) REFERENCES company_survey_grid_connection(id) ON DELETE RESTRICT ON UPDATE RESTRICT)
CREATE TABLE IF NOT EXISTS energielabel_pand (opnamedatum DATE NOT NULL, opnametype VARCHAR(1000) NULL, status VARCHAR(1000) NULL, berekeningstype VARCHAR(1000) NOT NULL, energieindex REAL NULL, energieklasse VARCHAR(1000) NULL, energielabel_is_prive VARCHAR(1000) NULL, is_op_basis_van_referentie_gebouw BOOLEAN NOT NULL, gebouwklasse VARCHAR(1000) NOT NULL, meting_geldig_tot DATE NOT NULL, registratiedatum DATE NOT NULL, postcode VARCHAR(1000) NULL, huisnummer BIGINT NULL, huisletter VARCHAR(1000) NULL, huisnummertoevoeging VARCHAR(1000) NULL, detailaanduiding VARCHAR(1000) NULL, bagverblijfsobjectid VARCHAR(1000) NULL, bagligplaatsid VARCHAR(1000) NULL, bagstandplaatsid VARCHAR(1000) NULL, bagpandid VARCHAR(1000) NULL, gebouwtype VARCHAR(1000) NULL, gebouwsubtype VARCHAR(1000) NULL, projectnaam VARCHAR(1000) NULL, projectobject VARCHAR(1000) NULL, "SBIcode" VARCHAR(1000) NULL, gebruiksoppervlakte_thermische_zone REAL NULL, energiebehoefte REAL NULL, eis_energiebehoefte REAL NULL, primaire_fossiele_energie REAL NULL, eis_primaire_fossiele_energie REAL NULL, "primaire_fossiele_energie_EMG_forfaitair" REAL NULL, aandeel_hernieuwbare_energie REAL NULL, eis_aandeel_hernieuwbare_energie REAL NULL, "aandeel_hernieuwbare_energie_EMG_forfaitair" REAL NULL, temperatuuroverschrijding REAL NULL, eis_temperatuuroverschrijding REAL NULL, warmtebehoefte REAL NULL, "energieindex_met_EMG_forfaitair" REAL NULL, CONSTRAINT chk_energielabel_pand_unsigned_huisnummer CHECK (huisnummer BETWEEN 0 AND 4294967295))