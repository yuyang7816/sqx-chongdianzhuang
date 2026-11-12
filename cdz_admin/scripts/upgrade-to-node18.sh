#!/usr/bin/env bash
# admin 管理端：Node 11 兼容栈 → Node 18 升级脚本
# 用法（在 admin 目录）：
#   bash scripts/upgrade-to-node18.sh
#   或  npm run upgrade:node18
set -euo pipefail

ROOT="$(cd "$(dirname "$0")/.." && pwd)"
cd "$ROOT"

TARGET_NODE_MAJOR=18
OPENSSL_FLAG="--openssl-legacy-provider"

echo "==> [1/5] 切换到 Node ${TARGET_NODE_MAJOR}"
if [ -s "${NVM_DIR:-$HOME/.nvm}/nvm.sh" ]; then
  # shellcheck disable=SC1090
  . "${NVM_DIR:-$HOME/.nvm}/nvm.sh"
  nvm install "$TARGET_NODE_MAJOR"
  nvm use
elif command -v fnm >/dev/null 2>&1; then
  fnm use || fnm install "$TARGET_NODE_MAJOR"
elif command -v n >/dev/null 2>&1; then
  n "$TARGET_NODE_MAJOR"
fi

NODE_VER="$(node -v | sed 's/^v//')"
NODE_MAJOR="${NODE_VER%%.*}"
if [ "$NODE_MAJOR" -lt "$TARGET_NODE_MAJOR" ] || [ "$NODE_MAJOR" -ge 19 ]; then
  echo "当前 Node 为 v${NODE_VER}，请使用 Node ${TARGET_NODE_MAJOR}.x（可用 nvm use）"
  exit 1
fi
echo "    Node $(node -v) / npm $(npm -v)"

echo "==> [2/5] 确认关键升级改动已就位"
need_files=(.nvmrc gulpfile.js build/utils.js package.json)
for f in "${need_files[@]}"; do
  [ -f "$f" ] || { echo "缺少 $f"; exit 1; }
done
grep -q 'openssl-legacy-provider' package.json || { echo "package.json 未配置 openssl-legacy-provider"; exit 1; }
grep -q 'gulp.series' gulpfile.js || { echo "gulpfile.js 未迁移到 Gulp 4"; exit 1; }
grep -q '"sass"' package.json || { echo "package.json 未使用 dart-sass"; exit 1; }
grep -q 'node-sass' package.json && { echo "package.json 仍含 node-sass，请移除"; exit 1; }
test -f .babelrc || { echo "缺少 .babelrc"; exit 1; }
test -f .postcssrc.js || { echo "缺少 .postcssrc.js"; exit 1; }
test -f .eslintrc.js || { echo "缺少 .eslintrc.js"; exit 1; }
echo "    配置检查通过"

echo "==> [3/5] 清理旧依赖锁（Node11 时代 shrinkwrap/lock）"
rm -rf node_modules
rm -f package-lock.json npm-shrinkwrap.json

echo "==> [4/5] npm install"
# 国内默认走 npmmirror，可通过 NPM_REGISTRY 覆盖
REGISTRY="${NPM_REGISTRY:-https://registry.npmmirror.com}"
npm install --legacy-peer-deps --registry="$REGISTRY"

echo "==> [5/5] 冒烟检查"
node -e "require('sass'); require('gulp'); console.log('sass/gulp OK')"
export NODE_OPTIONS="${OPENSSL_FLAG}"
node -e "require('crypto').createHash('md4'); console.log('openssl-legacy OK')"

echo ""
echo "升级完成。"
echo "  nvm use        # .nvmrc → Node ${TARGET_NODE_MAJOR}"
echo "  npm run dev    # 开发"
echo "  npm run build  # 生产构建"
