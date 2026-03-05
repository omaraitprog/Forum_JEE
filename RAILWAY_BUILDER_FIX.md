# Fix: Railway Using Railpack Instead of Dockerfile

## Problem
Railway dashboard shows "Railpack" as the builder instead of "Dockerfile", causing build failures.

## Solution: Change Builder in Railway Dashboard

**You MUST manually change the builder in Railway Dashboard:**

1. Go to [Railway Dashboard](https://railway.app)
2. Select your project
3. Click on your service
4. Go to **Settings** tab
5. Scroll to **Build & Deploy** section
6. Under **Builder**, change from "Railpack" to **"Dockerfile"**
7. Set **Dockerfile Path** to: `Dockerfile`
8. Click **Save** or the changes will auto-save
9. Railway will automatically trigger a new deployment

## Why This Happens

Railway auto-detects the build system. Even though we have:
- ✅ `Dockerfile` present
- ✅ `railway.toml` configured for Dockerfile
- ✅ `railway.json` configured for Dockerfile
- ✅ Removed `railpack.json` and `nixpacks.toml`

Railway might still default to Railpack if it was previously configured that way. The dashboard setting takes precedence.

## Verification

After changing the builder:
- Build logs should show: `FROM maven:3.9-eclipse-temurin-21 AS build`
- You should NOT see: `all mise packages: java cached` or Railpack-related messages
- Build should complete successfully with Docker

## Alternative: Railway CLI

If you have Railway CLI installed:
```bash
railway link
railway variables set RAILWAY_BUILDER=dockerfile
railway up
```

But the dashboard method is the most reliable.
