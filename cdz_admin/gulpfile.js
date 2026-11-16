var gulp = require('gulp');
var $    = require('gulp-load-plugins')();
var path = require('path');
var del  = require('del');
var execSync = require('child_process').execSync;

var distPath    = path.resolve('./dist');
var version     = ''; // 版本号
var versionPath = ''; // 版本号路径
var env         = ''; // 运行环境

// 创建版本号(年月日时分)
// (function () {
//   var d = new Date();
//   var yy = d.getFullYear().toString().slice(2);
//   var MM = d.getMonth() + 1 >= 10 ? (d.getMonth() + 1) : '0' + (d.getMonth() + 1);
//   var DD = d.getDate() >= 10 ? d.getDate() : '0' + d.getDate();
//   var h  = d.getHours() >= 10 ? d.getHours() : '0' + d.getHours();
//   var mm = d.getMinutes() >= 10 ? d.getMinutes() : '0' + d.getMinutes();
//   version = yy + MM + DD + h + mm;
//   versionPath = distPath + '/' + version;
// })();
(function () {
  version = 'vue';
  versionPath = distPath + '/' + version;
})();

// 编译（不用 gulp-shell，避免 lodash.template@4.18 assignWith 报错）
gulp.task('build', function (done) {
  try {
    execSync('node build/build.js', { stdio: 'inherit', env: process.env });
    done();
  } catch (err) {
    done(err);
  }
});

// 创建版本号目录
gulp.task('create:versionCatalog', function () {
  return gulp.src(distPath + '/static/**/*')
    .pipe(gulp.dest(versionPath + '/static/'))
});

// 替换${versionPath}/static/js/manifest.js window.SITE_CONFIG.cdnUrl占位变量
gulp.task('replace:cdnUrl', function () {
  return gulp.src(versionPath + '/static/js/manifest.js')
    .pipe($.replace(new RegExp('"' + require('./config').build.assetsPublicPath + '"', 'g'), 'window.SITE_CONFIG.cdnUrl + "/"'))
    .pipe(gulp.dest(versionPath + '/static/js/'))
});

// 替换${versionPath}/static/config/index-${env}.js window.SITE_CONFIG['version']配置变量
gulp.task('replace:version', function () {
  return gulp.src(versionPath + '/static/config/index-' + env + '.js')
    .pipe($.replace(/window.SITE_CONFIG\['version'\] = '.*'/g, "window.SITE_CONFIG['version'] = '" + version + "'"))
    .pipe(gulp.dest(versionPath + '/static/config/'))
});

// 合并${versionPath}/static/config/[index-${env}, init].js 至 ${distPath}/config/index.js
gulp.task('concat:config', function () {
  return gulp.src([versionPath + '/static/config/index-' + env + '.js', versionPath + '/static/config/init.js'])
    .pipe($.concat('index.js'))
    .pipe(gulp.dest(distPath + '/config/'))
});

gulp.task('clean', function () {
  return del([versionPath])
});

gulp.task('set-env', function (done) {
  env = process.env.npm_config_qa ? 'qa' : process.env.npm_config_uat ? 'uat' : 'prod'
  done()
});

gulp.task('clean:after', function () {
  return del([distPath + '/static', versionPath + '/static/config'])
});

// Gulp 4：用 series/parallel 替代依赖数组与 gulp.start
gulp.task('default', gulp.series(
  'clean',
  'set-env',
  'build',
  'create:versionCatalog',
  gulp.parallel('replace:cdnUrl', 'replace:version'),
  'concat:config',
  'clean:after'
));
