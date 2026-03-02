# Fix: JAVA_HOME Error on Railway Deployment

## Problem
Railway is using Nixpacks instead of Dockerfile, causing a `JAVA_HOME is not defined correctly` error.

## Solution 1: Force Railway to Use Dockerfile (Recommended)

1. **Go to Railway Dashboard:**
   - Open your project on [railway.app](https://railway.app)
   - Click on your web service

2. **Configure Builder:**
   - Go to **Settings** → **Build & Deploy**
   - Under **Builder**, select **"Dockerfile"**
   - Set **Dockerfile Path** to `Dockerfile`
   - Save the changes

3. **Redeploy:**
   - Railway will automatically trigger a new deployment
   - Or manually trigger: **Deployments** → **Redeploy**

## Solution 2: Fix Nixpacks Configuration (Temporary Workaround)

If Railway still uses Nixpacks, the `nixpacks.toml` file has been updated to fix the JAVA_HOME issue. However, **Dockerfile is strongly recommended** for Tomcat applications.

## Why Dockerfile is Better

- Your application is a WAR file that needs Tomcat
- Dockerfile already includes Tomcat and proper Java configuration
- More control over the build and deployment process
- Better suited for production deployments

## Verification

After configuring Dockerfile, check the build logs:
- You should see: `FROM maven:3.9-eclipse-temurin-21 AS build`
- You should NOT see: `all mise packages: java cached`

If you still see mise/Nixpacks logs, Railway hasn't switched to Dockerfile yet. Try:
1. Delete and recreate the service
2. Or use Railway CLI: `railway up --dockerfile Dockerfile`
