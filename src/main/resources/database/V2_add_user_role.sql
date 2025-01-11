-- add role column if not exists
ALTER TABLE public.users
    ADD COLUMN IF NOT EXISTS role VARCHAR(255);

-- set all as "ROLE_USER"
UPDATE public.users
SET role = 'ROLE_USER'
WHERE role IS NULL;

UPDATE public.users
SET role = 'ROLE_ADMIN'
WHERE email = 'csekas@ctrlspace.dev';