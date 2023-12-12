-- formatted using pgFormatter pg_format
CREATE TYPE KleinverbruikElectricityConnectionCapacity AS ENUM (
    '1x25A',
    '1x35A',
    '1x40A',
    '1x50A',
    '3x25A',
    '3x35A',
    '3x50A',
    '3x63A',
    '3x80A'
);

CREATE TYPE KleinverbruikElectricityConsumptionProfile AS ENUM (
    'ONE',
    'TWO',
    'THREE',
    'FOUR'
);

CREATE TYPE HeatingType AS ENUM (
    'GAS_BOILER',
    'ELECTRIC_HEATPUMP',
    'HYBRID_HEATPUMP',
    'DISTRICT_HEATING',
    'OTHER'
);

CREATE TYPE PVOrientation AS ENUM (
    'SOUTH',
    'EAST_WEST'
);

CREATE TYPE BlobPurpose AS ENUM (
    'NATURAL_GAS_VALUES',
    'ELECTRICITY_VALUES',
    'ELECTRICITY_AUTHORIZATION'
);

CREATE TABLE IF NOT EXISTS company_survey (
    id uuid PRIMARY KEY,
    created_at timestamp DEFAULT CURRENT_TIMESTAMP NOT NULL,
    energieke_regio_id bigint NULL,
    project varchar(50) NOT NULL,
    company_name varchar(50) NOT NULL,
    person_name varchar(50) NOT NULL,
    email varchar(50) NOT NULL,
    CONSTRAINT chk_company_survey_unsigned_energieke_regio_id CHECK (energieke_regio_id BETWEEN 0 AND 4294967295)
);

CREATE TABLE IF NOT EXISTS address (
    id uuid PRIMARY KEY,
    survey_id uuid NOT NULL,
    street varchar(50) NOT NULL,
    house_number bigint NOT NULL,
    house_letter varchar(1) NOT NULL,
    house_number_addition varchar(50) NOT NULL,
    postal_code varchar(8) NOT NULL,
    city varchar(50) NOT NULL,
    CONSTRAINT fk_address_survey_id__id FOREIGN KEY (survey_id) REFERENCES company_survey (id) ON DELETE RESTRICT ON UPDATE RESTRICT,
    CONSTRAINT chk_address_unsigned_house_number CHECK (house_number BETWEEN 0 AND 4294967295)
);

CREATE TABLE IF NOT EXISTS grid_connection (
    id uuid PRIMARY KEY,
    address_id uuid NOT NULL,
    energy_or_building_management_system_supplier varchar(1000) NOT NULL,
    main_consumption_process varchar(1000) NOT NULL,
    consumption_flexibility varchar(1000) NOT NULL,
    electrification_plans varchar(1000) NOT NULL,
    survey_feedback varchar(1000) NOT NULL,
    electricity_ean varchar(18) NOT NULL,
    annual_electricity_demand_kwh bigint NULL,
    annual_electricity_production_kwh bigint NULL,
    kleinverbuik_electricity_connection_capacity KleinverbruikElectricityConnectionCapacity NULL,
    kleinverbuik_electricity_consumption_profile KleinverbruikElectricityConsumptionProfile NULL,
    grootverbruik_contracted_demand_capacity_kw bigint NULL,
    grootverbruik_contracted_supply_capacity_kw bigint NULL,
    has_supply boolean NULL,
    pv_installed_kwp bigint NULL,
    pv_orientation PVOrientation NULL,
    pv_planned boolean NULL,
    pv_planned_kwp bigint NULL,
    pv_planned_orientation PVOrientation NULL,
    pv_planned_year bigint NULL,
    wind_installed_kw real NULL,
    other_supply varchar(1000) NOT NULL,
    has_natural_gas_connection boolean NULL,
    natural_gas_ean varchar(18) NOT NULL,
    natural_gas_annual_demand_m3 bigint NULL,
    percentage_natural_gas_for_heating bigint NULL,
    heating_types HeatingType ARRAY NOT NULL,
    combined_gas_boiler_kw real NULL,
    combined_heat_pump_kw real NULL,
    combined_hybrid_heat_pump_electric_kw real NULL,
    annual_district_heating_demand_gj real NULL,
    local_heat_exchange_description varchar(1000) NOT NULL,
    has_unused_residual_heat boolean NULL,
    has_battery boolean NULL,
    battery_capacity_kwh real NULL,
    battery_power_kw real NULL,
    battery_schedule varchar(1000) NOT NULL,
    has_planned_battery boolean NULL,
    planned_battery_capacity_kwh real NULL,
    planned_battery_power_kw real NULL,
    planned_battery_schedule varchar(1000) NOT NULL,
    has_thermal_storage boolean NULL,
    has_vehicles boolean NULL,
    num_daily_car_commuters bigint NULL,
    num_trucks bigint NULL,
    num_electric_trucks bigint NULL,
    num_truck_charge_points bigint NULL,
    power_per_truck_charge_point_kw real NULL,
    annual_travel_distance_per_truck_km bigint NULL,
    num_planned_electric_trucks bigint NULL,
    num_vans bigint NULL,
    num_electric_vans bigint NULL,
    num_electric_van_charge_points bigint NULL,
    power_per_van_charge_point_kw real NULL,
    annual_travel_distance_per_van_km bigint NULL,
    num_planned_electric_vans bigint NULL,
    num_cars bigint NULL,
    num_electric_cars bigint NULL,
    num_car_charge_points bigint NULL,
    power_per_car_charge_point_kw real NULL,
    annual_travel_distance_per_car_km bigint NULL,
    num_planned_electric_cars bigint NULL,
    CONSTRAINT fk_grid_connection_address_id__id FOREIGN KEY (address_id) REFERENCES address (id) ON DELETE RESTRICT ON UPDATE RESTRICT,
    CONSTRAINT chk_grid_connection_unsigned_annual_electricity_demand_kwh CHECK (annual_electricity_demand_kwh BETWEEN 0 AND 4294967295),
    CONSTRAINT chk_grid_connection_unsigned_annual_electricity_production_kwh CHECK (annual_electricity_production_kwh BETWEEN 0 AND 4294967295),
    CONSTRAINT chk_grid_connection_unsigned_grootverbruik_contracted_demand_ca CHECK (grootverbruik_contracted_demand_capacity_kw BETWEEN 0 AND 4294967295),
    CONSTRAINT chk_grid_connection_unsigned_grootverbruik_contracted_supply_ca CHECK (grootverbruik_contracted_supply_capacity_kw BETWEEN 0 AND 4294967295),
    CONSTRAINT chk_grid_connection_unsigned_pv_installed_kwp CHECK (pv_installed_kwp BETWEEN 0 AND 4294967295),
    CONSTRAINT chk_grid_connection_unsigned_pv_planned_kwp CHECK (pv_planned_kwp BETWEEN 0 AND 4294967295),
    CONSTRAINT chk_grid_connection_unsigned_pv_planned_year CHECK (pv_planned_year BETWEEN 0 AND 4294967295),
    CONSTRAINT chk_grid_connection_unsigned_natural_gas_annual_demand_m3 CHECK (natural_gas_annual_demand_m3 BETWEEN 0 AND 4294967295),
    CONSTRAINT chk_grid_connection_unsigned_percentage_natural_gas_for_heating CHECK (percentage_natural_gas_for_heating BETWEEN 0 AND 4294967295),
    CONSTRAINT chk_grid_connection_unsigned_num_daily_car_commuters CHECK (num_daily_car_commuters BETWEEN 0 AND 4294967295),
    CONSTRAINT chk_grid_connection_unsigned_num_trucks CHECK (num_trucks BETWEEN 0 AND 4294967295),
    CONSTRAINT chk_grid_connection_unsigned_num_electric_trucks CHECK (num_electric_trucks BETWEEN 0 AND 4294967295),
    CONSTRAINT chk_grid_connection_unsigned_num_truck_charge_points CHECK (num_truck_charge_points BETWEEN 0 AND 4294967295),
    CONSTRAINT chk_grid_connection_unsigned_annual_travel_distance_per_truck_k CHECK (annual_travel_distance_per_truck_km BETWEEN 0 AND 4294967295),
    CONSTRAINT chk_grid_connection_unsigned_num_planned_electric_trucks CHECK (num_planned_electric_trucks BETWEEN 0 AND 4294967295),
    CONSTRAINT chk_grid_connection_unsigned_num_vans CHECK (num_vans BETWEEN 0 AND 4294967295),
    CONSTRAINT chk_grid_connection_unsigned_num_electric_vans CHECK (num_electric_vans BETWEEN 0 AND 4294967295),
    CONSTRAINT chk_grid_connection_unsigned_num_electric_van_charge_points CHECK (num_electric_van_charge_points BETWEEN 0 AND 4294967295),
    CONSTRAINT chk_grid_connection_unsigned_annual_travel_distance_per_van_km CHECK (annual_travel_distance_per_van_km BETWEEN 0 AND 4294967295),
    CONSTRAINT chk_grid_connection_unsigned_num_planned_electric_vans CHECK (num_planned_electric_vans BETWEEN 0 AND 4294967295),
    CONSTRAINT chk_grid_connection_unsigned_num_cars CHECK (num_cars BETWEEN 0 AND 4294967295),
    CONSTRAINT chk_grid_connection_unsigned_num_electric_cars CHECK (num_electric_cars BETWEEN 0 AND 4294967295),
    CONSTRAINT chk_grid_connection_unsigned_num_car_charge_points CHECK (num_car_charge_points BETWEEN 0 AND 4294967295),
    CONSTRAINT chk_grid_connection_unsigned_annual_travel_distance_per_car_km CHECK (annual_travel_distance_per_car_km BETWEEN 0 AND 4294967295),
    CONSTRAINT chk_grid_connection_unsigned_num_planned_electric_cars CHECK (num_planned_electric_cars BETWEEN 0 AND 4294967295)
);

CREATE TABLE IF NOT EXISTS file (
    grid_connection_id uuid NOT NULL,
    purpose BlobPurpose NOT NULL,
    remote_name varchar(1000) PRIMARY KEY,
    original_name varchar(100) NOT NULL,
    content_type varchar(100) NULL,
    "size" int NOT NULL,
    CONSTRAINT fk_file_grid_connection_id__id FOREIGN KEY (grid_connection_id) REFERENCES grid_connection (id) ON DELETE RESTRICT ON UPDATE RESTRICT
);

CREATE TABLE IF NOT EXISTS energielabel_pand (
    opnamedatum date NOT NULL,
    opnametype varchar(1000) NULL,
    status varchar(1000) NULL,
    berekeningstype varchar(1000) NOT NULL,
    energieindex real NULL,
    energieklasse varchar(1000) NULL,
    energielabel_is_prive varchar(1000) NULL,
    is_op_basis_van_referentie_gebouw boolean NOT NULL,
    gebouwklasse varchar(1000) NOT NULL,
    meting_geldig_tot date NOT NULL,
    registratiedatum date NOT NULL,
    postcode varchar(1000) NULL,
    huisnummer bigint NULL,
    huisletter varchar(1000) NULL,
    huisnummertoevoeging varchar(1000) NULL,
    detailaanduiding varchar(1000) NULL,
    bagverblijfsobjectid varchar(1000) NULL,
    bagligplaatsid varchar(1000) NULL,
    bagstandplaatsid varchar(1000) NULL,
    bagpandid varchar(1000) NULL,
    gebouwtype varchar(1000) NULL,
    gebouwsubtype varchar(1000) NULL,
    projectnaam varchar(1000) NULL,
    projectobject varchar(1000) NULL,
    "SBIcode" varchar(1000) NULL,
    gebruiksoppervlakte_thermische_zone real NULL,
    energiebehoefte real NULL,
    eis_energiebehoefte real NULL,
    primaire_fossiele_energie real NULL,
    eis_primaire_fossiele_energie real NULL,
    "primaire_fossiele_energie_EMG_forfaitair" real NULL,
    aandeel_hernieuwbare_energie real NULL,
    eis_aandeel_hernieuwbare_energie real NULL,
    "aandeel_hernieuwbare_energie_EMG_forfaitair" real NULL,
    temperatuuroverschrijding real NULL,
    eis_temperatuuroverschrijding real NULL,
    warmtebehoefte real NULL,
    "energieindex_met_EMG_forfaitair" real NULL,
    CONSTRAINT chk_energielabel_pand_unsigned_huisnummer CHECK (huisnummer BETWEEN 0 AND 4294967295)
);

