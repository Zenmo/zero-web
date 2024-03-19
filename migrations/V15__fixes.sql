-- sync up some mistakes
ALTER TABLE IF EXISTS public.grid_connection
    ALTER COLUMN expansion_plans SET NOT NULL;

ALTER TABLE IF EXISTS public.grid_connection
    ALTER COLUMN expansion_request_reason SET NOT NULL;
