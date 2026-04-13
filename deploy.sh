#!/bin/bash

# Exit script if any command fails
set -e

echo "==================================="
echo "🚀 Orbit: Auto-Commit & Deploy"
echo "==================================="

echo "1. Staging and Committing all codebase changes..."
git add .
git commit -m "Auto-deploy update: $(date +"%Y-%m-%d %H:%M:%S")" || echo "No new changes to commit."

echo "2. Pushing to GitHub (Origin/Main)..."
git push origin main || echo "Push failed, but continuing local deployment..."

echo "3. Re-building entire Full Stack container architecture..."
sudo docker compose down
sudo docker compose up -d --build

echo ""
echo "==================================="
echo "✅ DEPLOYMENT SUCCESSFUL!"
echo "📍 Backend API & Swagger: http://localhost:9090"
echo "📍 Glassmorphic Frontend: http://localhost:5500"
echo "==================================="
