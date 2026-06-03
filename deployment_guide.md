# Gym Management Application - Deployment Guide

This guide outlines how to deploy your Gym Management Spring Boot application to various cloud providers and connect it to a production **Supabase PostgreSQL database**.

---

## Step 1: Initialize Your Supabase Database

Before deploying the application, you need to set up the database schema on Supabase:

1. Log in to [Supabase](https://supabase.com/).
2. Create a new project or select an existing one.
3. In the sidebar, navigate to the **SQL Editor**.
4. Click **New Query**.
5. Copy the contents of your [schema.sql](file:///c:/Users/madha/OneDrive/Desktop/fitness%20tracker/schema.sql) file and paste it into the editor.
6. Click **Run**. Verify that the tables (`membership_plans`, `members`, `payments`, `workout_schedules`) are successfully created.

---

## Step 2: Retrieve Supabase Connection Details

To connect your Spring Boot application to Supabase:

1. In Supabase, go to **Project Settings** > **Database**.
2. Under **Connection string**, select **URI** or **JDBC**.
3. Locate the details:
   - **Host** (e.g., `aws-0-us-east-1.pooler.supabase.com`)
   - **Database Name** (usually `postgres`)
   - **Port** (usually `5432` or `6543` for transaction pooling)
   - **Username** (usually `postgres` or `postgres.your-project-id`)
   - **Password** (the database password you set during project creation)
4. Your JDBC Connection URL will look like this:
   `jdbc:postgresql://<DB-HOST>:<PORT>/postgres?sslmode=require`

---

## Step 3: Deploying to Cloud Providers

Choose one of the hosting providers below to deploy your application.

### Option A: Render (Recommended)

Render is highly recommended because it can automatically build and run the application using the included [Dockerfile](file:///c:/Users/madha/OneDrive/Desktop/fitness%20tracker/Dockerfile).

1. Commit and push the new files (`Dockerfile`, `.dockerignore`, etc.) to your GitHub repository.
2. Sign in to [Render](https://render.com/).
3. Click **New +** and select **Web Service**.
4. Connect your GitHub repository.
5. In the creation form:
   - **Name**: `gym-fitness-tracker`
   - **Region**: Select a region close to your Supabase database.
   - **Runtime**: Select **Docker** (Render will automatically detect the `Dockerfile` at the root).
   - **Instance Type**: Select **Free** (or a paid tier if preferred).
6. Click **Advanced** and add the following **Environment Variables**:
   - `SPRING_PROFILES_ACTIVE` = `supabase`
   - `SPRING_DATASOURCE_URL` = `jdbc:postgresql://<DB-HOST>:<PORT>/postgres?sslmode=require`
   - `SPRING_DATASOURCE_USERNAME` = `<YOUR-SUPABASE-USERNAME>`
   - `SPRING_DATASOURCE_PASSWORD` = `<YOUR-SUPABASE-PASSWORD>`
7. Click **Create Web Service**. Render will build the Docker container and deploy the app!

---

### Option B: Heroku

Heroku will use the [Procfile](file:///c:/Users/madha/OneDrive/Desktop/fitness%20tracker/Procfile) and [system.properties](file:///c:/Users/madha/OneDrive/Desktop/fitness%20tracker/system.properties) to build and run the JAR.

1. Commit and push all changes to your GitHub repository.
2. Log in to your [Heroku Dashboard](https://dashboard.heroku.com/).
3. Click **New** > **Create new app**.
4. Go to the **Settings** tab, scroll down to **Config Vars**, click **Reveal Config Vars**, and add:
   - `SPRING_PROFILES_ACTIVE` = `supabase`
   - `SPRING_DATASOURCE_URL` = `jdbc:postgresql://<DB-HOST>:<PORT>/postgres?sslmode=require`
   - `SPRING_DATASOURCE_USERNAME` = `<YOUR-SUPABASE-USERNAME>`
   - `SPRING_DATASOURCE_PASSWORD` = `<YOUR-SUPABASE-PASSWORD>`
5. Go to the **Deploy** tab:
   - Select **GitHub** as the deployment method.
   - Search for your repository and connect it.
   - Scroll down and click **Deploy Branch** (e.g., `main`).
6. Heroku will build the Maven project and launch the application.

---

### Option C: Fly.io

Fly.io lets you run Dockerized apps close to your users.

1. Install the [Fly CLI](https://fly.io/docs/hands-on/install-cli/) on your machine.
2. Open your terminal in the project root directory and run:
   ```bash
   fly launch
   ```
3. The Fly CLI will detect the `Dockerfile` and ask you to configure the application name, region, and whether you want to deploy a database (select No, since we are using Supabase).
4. Before deploying, set your secrets (environment variables) via Fly CLI:
   ```bash
   fly secrets set SPRING_PROFILES_ACTIVE=supabase \
     SPRING_DATASOURCE_URL="jdbc:postgresql://<DB-HOST>:<PORT>/postgres?sslmode=require" \
     SPRING_DATASOURCE_USERNAME="<YOUR-SUPABASE-USERNAME>" \
     SPRING_DATASOURCE_PASSWORD="<YOUR-SUPABASE-PASSWORD>"
   ```
5. Deploy the application:
   ```bash
   fly deploy
   ```

---

## Step 4: Verification

Once deployment completes, open the provided application URL in your browser:
- You should see the login screen.
- You can access pages like `/members`, `/schedules`, `/payments`, and `/dashboard`.
- Any data you add or update in the application will be written to and read from your Supabase PostgreSQL database in real-time.
