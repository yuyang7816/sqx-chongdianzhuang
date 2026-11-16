<template>
	<div class="chart-container">
		<!-- 图例 -->
		<div class="legend">
			<div class="legend-item" v-for="(item,index) in chartData" :key="index">
				<div class="legend-color" :style="{backgroundColor:item.color}"></div>
				<span>{{item.name}}</span>
			</div>
		</div>

		<!-- 环形图 -->
		<div class="chart">
			<!-- #ifdef H5 -->
			<canvas ref="donutChartRef" canvas-id="donutChart"
				:style="{ width: canvasWidth + 'px', height: canvasHeight + 'px' }" id="donutChart"></canvas>
			<!-- #endif -->

			<!-- #ifndef H5 -->
			<canvas ref="donutChartRef" canvas-id="donutChart"
				:style="{ width: canvasWidth + 'px', height: canvasHeight + 'px' }" id="donutChart"></canvas>
			<!-- #endif -->
		</div>
	</div>
</template>

<script>
	export default {
		name: 'DonutChart',
		props: {
			data: {
				type: Array,
				default: () => []
			}
		},
		data() {
			return {
				canvasWidth: uni.upx2px(400),
				canvasHeight: uni.upx2px(400),
				centerX: uni.upx2px(200),
				centerY: uni.upx2px(200),
				outerRadius: uni.upx2px(160),
				innerRadius: uni.upx2px(80),
				chartData: []
			}
		},
		watch: {
			data: {
				handler(newVal) {
					if (newVal && newVal.length > 0) {
						this.chartData = newVal;
						this.$nextTick(() => {
							this.drawChart();
						});
					}
				},
				immediate: true
			}
		},
		mounted() {
			this.initData();
			this.$nextTick(() => {
				setTimeout(() => {
					this.drawChart();
				}, 100);
			});
		},
		methods: {
			initData() {
				if (this.data && this.data.length > 0) {
					this.chartData = this.data;
				} else {
					this.chartData = [{
							name: '微信',
							value: 37.3,
							color: '#4CAF50'
						},
						{
							name: '美团',
							value: 27.8,
							color: '#FFC107'
						},
						{
							name: '抖音团购',
							value: 34.9,
							color: '#E91E63'
						},
						{
							name: '其他',
							value: 0,
							color: '#FF9800'
						}
					];
				}
			},
			drawChart() {
				const ctx = uni.createCanvasContext('donutChart', this);

				if (!ctx) {
					console.error('Canvas context creation failed');
					return;
				}

				ctx.clearRect(0, 0, this.canvasWidth, this.canvasHeight);

				let startAngle = 0;

				this.chartData.forEach(item => {
					if (item.value <= 0) return;

					const endAngle = startAngle + (item.value / 100) * 2 * Math.PI;

					ctx.beginPath();
					ctx.arc(this.centerX, this.centerY, this.outerRadius, startAngle, endAngle);
					ctx.lineTo(this.centerX, this.centerY);
					ctx.closePath();

					ctx.setFillStyle(item.color);
					ctx.fill();

					this.drawTextLabel(ctx, startAngle, endAngle, item.value);

					startAngle = endAngle;
				});

				ctx.beginPath();
				ctx.arc(this.centerX, this.centerY, this.innerRadius, 0, 2 * Math.PI);
				ctx.setFillStyle('#FFFFFF');
				ctx.fill();

				ctx.draw(false, (res) => {
					if (res.errMsg) {
						console.error('Canvas draw failed:', res.errMsg);
					}
				});
			},

			drawTextLabel(ctx, startAngle, endAngle, value) {
				if (value <= 0) return;

				const angle = (startAngle + endAngle) / 2;
				const x = this.centerX + (this.outerRadius - 20) * Math.cos(angle);
				const y = this.centerY + (this.outerRadius - 20) * Math.sin(angle);

				ctx.save();
				ctx.setFontSize(12);
				ctx.setFillStyle('#333');
				ctx.setTextAlign('center');
				ctx.setTextBaseline('middle');

				ctx.setFillStyle('#FFFFFF');
				ctx.fillRect(x - 20, y - 10, 40, 20);
				ctx.setStrokeStyle('#CCCCCC');
				ctx.strokeRect(x - 20, y - 10, 40, 20);

				ctx.setFillStyle('#333');
				ctx.fillText(value + '%', x, y);
				ctx.restore();
			}
		}
	}
</script>

<style scoped>
	.chart-container {
		display: flex;
		align-items: center;
		background-color: #fff;
	}

	.legend {
		width: 150px;
		margin-right: 20px;
	}

	.legend-item {
		display: flex;
		align-items: center;
		margin-bottom: 15px;
	}

	.legend-color {
		width: 16px;
		height: 16px;
		border-radius: 50%;
		margin-right: 8px;
	}

	.chart {
		flex: 1;
		position: relative;
		display: flex;
		justify-content: center;
		align-items: center;
	}
</style>