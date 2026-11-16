<template>
	<div class="nf-page home-page">
		<div class="home-page__top">
			<div class="nf-page-hero home-page__hero">
				<span class="nf-page-hero__badge">数据中心</span>
				<h2 class="nf-page-hero__title">数据概览</h2>
				<p class="nf-page-hero__desc">查看平台用户、充值与订单核心指标，支持按天/月/年筛选各维度统计数据。</p>
				<div class="nf-page-hero__bar"></div>
			</div>
			<div class="nf-filter-card home-page__filter">
				<div class="nf-filter-card__head">
					<i class="el-icon-date nf-filter-card__head-icon"></i>
					<span>筛选条件</span>
				</div>
				<div class="home-page__filter-body">
					<el-select v-model="flag" class="home-page__filter-select" @change="orderfenxi">
						<el-option v-for="item in flags" :key="item.value" :label="item.label" :value="item.value">
						</el-option>
					</el-select>
					<el-date-picker
						class="home-page__filter-date"
						v-model="info.stockDate"
						align="right"
						type="date"
						format="yyyy-MM-dd"
						value-format="yyyy-MM-dd"
						placeholder="选择开始时间"
						@change="animeOrder">
					</el-date-picker>
				</div>
			</div>
		</div>

		<div class="home-page__kpi-grid">
			<div class="home-page__kpi-card">
				<div class="home-page__kpi-icon"><i class="el-icon-user"></i></div>
				<div class="home-page__kpi-body">
					<div class="home-page__kpi-label">总用户数</div>
					<div class="home-page__kpi-value"><span>{{ tableData.totalUsers ? tableData.totalUsers : 0 }}</span>人</div>
				</div>
			</div>
			<div class="home-page__kpi-card">
				<div class="home-page__kpi-icon"><i class="el-icon-plus"></i></div>
				<div class="home-page__kpi-body">
					<div class="home-page__kpi-label">今日新增</div>
					<div class="home-page__kpi-value"><span>{{ tableData.newToday ? tableData.newToday : 0 }}</span>人</div>
				</div>
			</div>
			<div class="home-page__kpi-card">
				<div class="home-page__kpi-icon"><i class="el-icon-coin"></i></div>
				<div class="home-page__kpi-body">
					<div class="home-page__kpi-label">总充值</div>
					<div class="home-page__kpi-value"><span>{{ tableData.totalRevenue ? tableData.totalRevenue : 0 }}</span>元</div>
				</div>
			</div>
			<div class="home-page__kpi-card">
				<div class="home-page__kpi-icon"><i class="el-icon-money"></i></div>
				<div class="home-page__kpi-body">
					<div class="home-page__kpi-label">今日充值</div>
					<div class="home-page__kpi-value"><span>{{ tableData.todayRevenue ? tableData.todayRevenue : 0 }}</span>元</div>
				</div>
			</div>
		</div>

		<div class="home-page__detail-cols">
			<div class="home-page__detail-col">
				<div class="home-page__detail-col-head">用户增长</div>
				<div class="home-page__stat-tiles">
					<div class="home-page__stat-tile">
						<div class="home-page__stat-tile-label">本月新增</div>
						<div class="home-page__stat-tile-value"><span>{{ tableData.newMonth ? tableData.newMonth : 0 }}</span>人</div>
					</div>
					<div class="home-page__stat-tile">
						<div class="home-page__stat-tile-label">本年新增</div>
						<div class="home-page__stat-tile-value"><span>{{ tableData.newYear ? tableData.newYear : 0 }}</span>人</div>
					</div>
				</div>
			</div>
			<div class="home-page__detail-col">
				<div class="home-page__detail-col-head">充值概览</div>
				<div class="home-page__stat-tiles">
					<div class="home-page__stat-tile">
						<div class="home-page__stat-tile-label">本月充值</div>
						<div class="home-page__stat-tile-value"><span>{{ tableData.monthRevenue ? tableData.monthRevenue : 0 }}</span>元</div>
					</div>
					<div class="home-page__stat-tile">
						<div class="home-page__stat-tile-label">本年充值</div>
						<div class="home-page__stat-tile-value"><span>{{ tableData.yearRevenue ? tableData.yearRevenue : 0 }}</span>元</div>
					</div>
					<div class="home-page__stat-tile">
						<div class="home-page__stat-tile-label">订单支付金额</div>
						<div class="home-page__stat-tile-value"><span>{{ tableData.ordersMoney ? tableData.ordersMoney : 0 }}</span>元</div>
					</div>
				</div>
			</div>
		</div>

		<div class="home-page__tabs-card">
			<el-tabs v-model="activeName" @tab-click="handleClick" class="home-page__tabs">
				<el-tab-pane label="用户分析" name="first">
					<el-row :gutter="12">
						<el-col :span="6" class="cards">
							<div class="box">
								<div class="box_num">
									<div class="box_color">总用户统计</div>
									<div class="text_color">
										<span>{{userStat.userCount ? userStat.userCount : 0 }}</span>人
									</div>
								</div>
							</div>
						</el-col>
						<el-col :span="6" class="cards">
							<div class="box">
								<div class="box_num">
									<div class="box_color">手机号用户</div>
									<div class="text_color">
										<span>{{userStat.userPhoneCount ? userStat.userPhoneCount : 0}}</span>人
									</div>
								</div>
							</div>
						</el-col>
						<el-col :span="6" class="cards">
							<div class="box">
								<div class="box_num">
									<div class="box_color">未绑定手机号用户</div>
									<div class="text_color">
										<span>{{userStat.userNullPhoneCount ? userStat.userNullPhoneCount : 0}}</span>人
									</div>
								</div>
							</div>
						</el-col>
						<!-- <el-col :span="6" class="cards">
							<div class="box">
								<div class="box_num">
									<div class="box_color">APP用户统计</div>
									<div class="text_color"><span>{{userStat.appCount ? userStat.appCount : 0}}</span>人
									</div>
								</div>
							</div>
						</el-col> -->
						<el-col :span="6" class="cards">
							<div class="box">
								<div class="box_num">
									<div class="box_color">小程序用户统计</div>
									<div class="text_color"><span>{{userStat.wxCount ? userStat.wxCount : 0}}</span>人
									</div>
								</div>
							</div>
						</el-col>
						<!-- <el-col :span="6" class="cards">
							<div class="box">
								<div class="box_num">
									<div class="box_color">公众号用户统计</div>
									<div class="text_color"><span>{{userStat.wx ? userStat.wx : 0}}</span>人</div>
								</div>
							</div>
						</el-col> -->
						<!-- <el-col :span="6" class="cards">
							<div class="box">
								<div class="box_num">
									<div class="box_color">保洁用户统计</div>
									<div class="text_color">
										<span>{{userStat.userTearoomCount ? userStat.userTearoomCount : 0}}</span>人
									</div>
								</div>
							</div>
						</el-col> -->
						<el-col :span="6" class="cards">
							<div class="box">
								<div class="box_num">
									<div class="box_color">充电站用户统计</div>
									<div class="text_color">
										<span>{{userStat.tearoomCount ? userStat.tearoomCount : 0}}</span>人
									</div>
								</div>
							</div>
						</el-col>
						<div v-if="userStat.vipList&&userStat.vipList.length>0">
							<el-col :span="6" class="cards" v-for="(item,index) in userStat.vipList " :key="index">
								<div class="box">
									<div class="box_num">
										<div class="box_color">{{item.vipName}}用户</div>
										<div class="text_color">
											<span>{{item.vipCount ? item.vipCount : 0}}</span>人
										</div>
									</div>
								</div>
							</el-col>
						</div>
					</el-row>
				</el-tab-pane>
				<el-tab-pane label="充电桩分析" name="third">
					<div class="nf-table-card home-page__table-wrap">
					<el-table v-loading="tableDataLoading" :data="taskStat.list">
						<el-table-column prop="tearoomName" label="充电站名称">
						</el-table-column>
						<el-table-column prop="tearoomCoverImage" label="充电站图片">
							<template slot-scope="scope">
								<div v-if="scope.row.tearoomCoverImage">
									<span v-for="(item,index) in scope.row.tearoomCoverImage.split(',')">
										<el-popover placement="top-start" title="" trigger="hover" v-if="index==0">
											<img style="width: 50px; height: 50px;margin: 3px;" :src="item" alt=""
												slot="reference">
											<img style="width: 300px; height: 300px" :src="item" alt="">
										</el-popover>
									</span>
								</div>
								<div v-else>暂无图片</div>
								<!-- <img :src="scope.row.tearoomCoverImage" width="60" height="60" /> -->
							</template>
						</el-table-column>
						<el-table-column prop="counts" label="地址">
							<template slot-scope="scope">
								<span>{{scope.row.tearoomProvince}}{{scope.row.tearoomCity}}{{scope.row.tearoomDistrict}}{{scope.row.tearoomAddress}}</span>
							</template>
						</el-table-column>
						<el-table-column prop="sumMoney" label="收益">
						</el-table-column>
					</el-table>
					<div class="home-page__pagination">
						<el-pagination @size-change="handleSizeChangeC" @current-change="handleCurrentChangeC"
							:page-sizes="[5, 10, 15, 20]" :page-size="limit" :current-page="page"
							layout="total,sizes, prev, pager, next,jumper" :total="taskStat.totalCount">
						</el-pagination>
					</div>
					</div>
				</el-tab-pane>
				<el-tab-pane label="支付统计" name="sixth">
					<!-- <div>
						<el-select v-model="flag" style="width:150px;margin-left: 10px;" @change="animeDat3">
							<el-option v-for="item in flags" :key="item.value" :label="item.label" :value="item.value">
							</el-option>
						</el-select>&nbsp;&nbsp;&nbsp;
						<el-date-picker style="width: 200px;margin-left: 10px;" v-model="info1.stockDate1" align="right"
							type="date" format="yyyy-MM-dd" value-format="yyyy-MM-dd" placeholder="选择开始时间"
							@change="animeSelect3">
						</el-date-picker>
					</div> -->
					<el-row :gutter="12">
						<el-col :span="6" class="cards">
							<div class="box">
								<div class="box_num">
									<div class="box_color">支付总金额</div>
									<div class="text_color">
										<span>{{rechgeData.sumMoney ? rechgeData.sumMoney : 0}}</span>元
									</div>
								</div>
							</div>
						</el-col>
						<!-- <el-col :span="6" class="cards">
							<div class="box">
								<div class="box_num">
									<div class="box_color">微信小程序支付金额</div>
									<div class="text_color">
										<span>{{rechgeData.weiXinXCXMoney ? rechgeData.weiXinXCXMoney : 0}}</span>元
									</div>
								</div>
							</div>
						</el-col>
						<el-col :span="6" class="cards">
							<div class="box">
								<div class="box_num">
									<div class="box_color">系统充值</div>
									<div class="text_color">
										<span>{{rechgeData.xiTongMoney ? rechgeData.xiTongMoney : 0}}</span>元
									</div>
								</div>
							</div>
						</el-col> -->
						<el-col :span="6" class="cards">
							<div class="box">
								<div class="box_num">
									<div class="box_color">微信小程序支付金额</div>
									<div class="text_color">
										<span>{{rechgeData.weiXinXCXMoney ? rechgeData.weiXinXCXMoney : 0}}</span>元
									</div>
								</div>
							</div>
						</el-col>
						<el-col :span="6" class="cards">
							<div class="box">
								<div class="box_num">
									<div class="box_color">微信APP支付金额</div>
									<div class="text_color">
										<span>{{rechgeData.weiXinAppMoney ? rechgeData.weiXinAppMoney : 0}}</span>元
									</div>
								</div>
							</div>
						</el-col>
						<el-col :span="6" class="cards">
							<div class="box">
								<div class="box_num">
									<div class="box_color">微信公众号支付金额</div>
									<div class="text_color">
										<span>{{rechgeData.weiXinGZHMoney ? rechgeData.weiXinGZHMoney : 0}}</span>元
									</div>
								</div>
							</div>
						</el-col>
						<el-col :span="6" class="cards">
							<div class="box">
								<div class="box_num">
									<div class="box_color">支付宝APP支付金额</div>
									<div class="text_color">
										<span>{{rechgeData.zhiFuBaoAppMoney ? rechgeData.zhiFuBaoAppMoney : 0}}</span>元
									</div>
								</div>
							</div>
						</el-col>
						<el-col :span="6" class="cards">
							<div class="box">
								<div class="box_num">
									<div class="box_color">支付宝H5支付金额</div>
									<div class="text_color">
										<span>{{rechgeData.zhiFuBaoH5Money ? rechgeData.zhiFuBaoH5Money : 0}}</span>元
									</div>
								</div>
							</div>
						</el-col>
						<el-col :span="6" class="cards">
							<div class="box">
								<div class="box_num">
									<div class="box_color">系统充值</div>
									<div class="text_color">
										<span>{{rechgeData.xiTongMoney ? rechgeData.xiTongMoney : 0}}</span>元
									</div>
								</div>
							</div>
						</el-col>
						<el-col :span="6" class="cards">
							<div class="box">
								<div class="box_num">
									<div class="box_color">退款金额</div>
									<div class="text_color">
										<span>{{rechgeData.refundMoney ? rechgeData.refundMoney : 0}}</span>元
									</div>
								</div>
							</div>
						</el-col>
					</el-row>
				</el-tab-pane>
				<!-- <el-tab-pane label="公益订单统计" name="youhuiquandingdan">
					<el-row :gutter="12">
						<el-col :span="6" class="cards">
							<div class="box">
								<div class="box_num">
									<div class="box_color">总订单数量</div>
									<div class="text_color">
										<span>{{shouyiOrData.ordersCount ? shouyiOrData.ordersCount : 0}}</span>单
									</div>
								</div>
							</div>
						</el-col>
						<el-col :span="6" class="cards">
							<div class="box">
								<div class="box_num">
									<div class="box_color">会员卡订单数量</div>
									<div class="text_color">
										<span>{{shouyiOrData.ordersMoneyCount ? shouyiOrData.ordersMoneyCount : 0}}</span>单
									</div>
								</div>
							</div>
						</el-col>
						<el-col :span="6" class="cards">
							<div class="box">
								<div class="box_num">
									<div class="box_color">团购订单数量</div>
									<div class="text_color">
										<span>{{shouyiOrData.ordersCouponCount ? shouyiOrData.ordersCouponCount : 0}}</span>单
									</div>
								</div>
							</div>
						</el-col>

					</el-row>
				</el-tab-pane>
				<el-tab-pane label="爱心钱包统计" name="aixinqianbao">
					<el-table v-loading="tableDataLoading" :data="tableDataAx.list">
						<el-table-column prop="userId" label="编号" width="80">
							<template slot-scope="scope">
								{{scope.$index+1}}
							</template>
						</el-table-column>
						<el-table-column prop="tearoomName" label="门店名称">
						</el-table-column>
						<el-table-column prop="money" label="爱心钱包">
						</el-table-column>
						<el-table-column prop="donationMoney" label="已捐款">
						</el-table-column>
					</el-table>
					<div style="text-align: center;margin-top: 10px;margin-bottom: 20px;">
						<el-pagination @size-change="handleSizeChangeT" @current-change="handleCurrentChangeT"
							:page-sizes="[10, 20, 30, 50, 100]" :page-size="limit" :current-page="page"
							layout="total,sizes, prev, pager, next" :total="tableDataAx.totalCount">
						</el-pagination>
					</div>
				</el-tab-pane>
				<el-tab-pane label="台球麻将ps系列订单统计" name="seven">
					<el-row :gutter="12">
						<el-col :span="6" class="cards">
							<div class="box">
								<div class="box_num">
									<div class="box_color">支付金额</div>
									<div class="text_color">
										<span>{{orderData.payMoney ? orderData.payMoney : 0}}</span>元
									</div>
								</div>
							</div>
						</el-col>
						<el-col :span="6" class="cards">
							<div class="box">
								<div class="box_num">
									<div class="box_color">结算金额</div>
									<div class="text_color">
										<span>{{orderData.shopMoney ? orderData.shopMoney : 0}}</span>元
									</div>
								</div>
							</div>
						</el-col>
						<el-col :span="6" class="cards">
							<div class="box">
								<div class="box_num">
									<div class="box_color">押金金额</div>
									<div class="text_color">
										<span>{{orderData.cashPledge ? orderData.cashPledge : 0}}</span>元
									</div>
								</div>
							</div>
						</el-col>
						<el-col :span="6" class="cards">
							<div class="box">
								<div class="box_num">
									<div class="box_color">待缴费金额</div>
									<div class="text_color">
										<span>{{orderData.sumMoney ? orderData.sumMoney : 0}}</span>元
									</div>
								</div>
							</div>
						</el-col>
						<el-col :span="6" class="cards">
							<div class="box">
								<div class="box_num">
									<div class="box_color">待支付</div>
									<div class="text_color">
										<span>{{orderData.dzfCount ? orderData.dzfCount : 0}}</span>单
									</div>
								</div>
							</div>
						</el-col>
						<el-col :span="6" class="cards">
							<div class="box">
								<div class="box_num">
									<div class="box_color">待使用</div>
									<div class="text_color">
										<span>{{orderData.dsyCount ? orderData.dsyCount : 0}}</span>单
									</div>
								</div>
							</div>
						</el-col>
						<el-col :span="6" class="cards">
							<div class="box">
								<div class="box_num">
									<div class="box_color">使用中</div>
									<div class="text_color">
										<span>{{orderData.syzCount ? orderData.syzCount : 0}}</span>单
									</div>
								</div>
							</div>
						</el-col>
						<el-col :span="6" class="cards">
							<div class="box">
								<div class="box_num">
									<div class="box_color">已完成</div>
									<div class="text_color">
										<span>{{orderData.ywcCount ? orderData.ywcCount : 0}}</span>单
									</div>
								</div>
							</div>
						</el-col>
					</el-row>
				</el-tab-pane>
				<el-tab-pane label="租柜订单统计" name="sevenZg">
					<el-row :gutter="12">
						<el-col :span="6" class="cards">
							<div class="box">
								<div class="box_num">
									<div class="box_color">支付金额</div>
									<div class="text_color">
										<span>{{orderData.payMoney ? orderData.payMoney : 0}}</span>元
									</div>
								</div>
							</div>
						</el-col>
						<el-col :span="6" class="cards">
							<div class="box">
								<div class="box_num">
									<div class="box_color">结算金额</div>
									<div class="text_color">
										<span>{{orderData.shopMoney ? orderData.shopMoney : 0}}</span>元
									</div>
								</div>
							</div>
						</el-col>
						<el-col :span="6" class="cards">
							<div class="box">
								<div class="box_num">
									<div class="box_color">押金金额</div>
									<div class="text_color">
										<span>{{orderData.cashPledge ? orderData.cashPledge : 0}}</span>元
									</div>
								</div>
							</div>
						</el-col>
						<el-col :span="6" class="cards">
							<div class="box">
								<div class="box_num">
									<div class="box_color">待缴费金额</div>
									<div class="text_color">
										<span>{{orderData.sumMoney ? orderData.sumMoney : 0}}</span>元
									</div>
								</div>
							</div>
						</el-col>
						<el-col :span="6" class="cards">
							<div class="box">
								<div class="box_num">
									<div class="box_color">待支付</div>
									<div class="text_color">
										<span>{{orderData.dzfCount ? orderData.dzfCount : 0}}</span>单
									</div>
								</div>
							</div>
						</el-col>
						<el-col :span="6" class="cards">
							<div class="box">
								<div class="box_num">
									<div class="box_color">待使用</div>
									<div class="text_color">
										<span>{{orderData.dsyCount ? orderData.dsyCount : 0}}</span>单
									</div>
								</div>
							</div>
						</el-col>
						<el-col :span="6" class="cards">
							<div class="box">
								<div class="box_num">
									<div class="box_color">使用中</div>
									<div class="text_color">
										<span>{{orderData.syzCount ? orderData.syzCount : 0}}</span>单
									</div>
								</div>
							</div>
						</el-col>
						<el-col :span="6" class="cards">
							<div class="box">
								<div class="box_num">
									<div class="box_color">已完成</div>
									<div class="text_color">
										<span>{{orderData.ywcCount ? orderData.ywcCount : 0}}</span>单
									</div>
								</div>
							</div>
						</el-col>
					</el-row>
				</el-tab-pane> -->
				<el-tab-pane label="收益统计" name="eight">
					<el-row :gutter="12">
						<el-col :span="6" class="cards">
							<div class="box">
								<div class="box_num">
									<div class="box_color">订单支付金额</div>
									<div class="text_color">
										<span>{{shouyiData.ordersPayMoney ? shouyiData.ordersPayMoney : 0}}</span>元
									</div>
								</div>
							</div>
						</el-col>
						<el-col :span="6" class="cards">
							<div class="box">
								<div class="box_num">
									<div class="box_color">商家收益</div>
									<div class="text_color">
										<span>{{shouyiData.ordersShopMoney ? shouyiData.ordersShopMoney : 0}}</span>元
									</div>
								</div>
							</div>
						</el-col>
						<el-col :span="6" class="cards">
							<div class="box">
								<div class="box_num">
									<div class="box_color">分销金额</div>
									<div class="text_color">
										<span>{{shouyiData.ordersFenMoney ? shouyiData.ordersFenMoney : 0}}</span>元
									</div>
								</div>
							</div>
						</el-col>
						<el-col :span="6" class="cards">
							<div class="box">
								<div class="box_num">
									<div class="box_color">平台收益</div>
									<div class="text_color">
										<span>{{shouyiData.ordersPingMoney ? shouyiData.ordersPingMoney : 0}}</span>元
									</div>
								</div>
							</div>
						</el-col>
					</el-row>
				</el-tab-pane>
				<el-tab-pane label="优惠券统计" name="nine">
					<el-row :gutter="12">
						<el-col :span="6" class="cards">
							<div class="box">
								<div class="box_num">
									<div class="box_color">新用户领取数量</div>
									<div class="text_color">
										<span>{{youhuiquanData.xyhCount ? youhuiquanData.xyhCount : 0}}</span>
									</div>
								</div>
							</div>
						</el-col>
						<el-col :span="6" class="cards">
							<div class="box">
								<div class="box_num">
									<div class="box_color">邀请新用户数量</div>
									<div class="text_color">
										<span>{{youhuiquanData.yqxhCount ? youhuiquanData.yqxhCount : 0}}</span>
									</div>
								</div>
							</div>
						</el-col>
						<el-col :span="6" class="cards">
							<div class="box">
								<div class="box_num">
									<div class="box_color">邀请用户下单数量</div>
									<div class="text_color">
										<span>{{youhuiquanData.yqxdCount ? youhuiquanData.yqxdCount : 0}}</span>
									</div>
								</div>
							</div>
						</el-col>
						<el-col :span="6" class="cards">
							<div class="box">
								<div class="box_num">
									<div class="box_color">支付购买数量</div>
									<div class="text_color">
										<span>{{youhuiquanData.zfgmCount ? youhuiquanData.zfgmCount : 0}}</span>
									</div>
								</div>
							</div>
						</el-col>
						<el-col :span="6" class="cards">
							<div class="box">
								<div class="box_num">
									<div class="box_color">积分兑换数量</div>
									<div class="text_color">
										<span>{{youhuiquanData.jfdhCount ? youhuiquanData.jfdhCount : 0}}</span>
									</div>
								</div>
							</div>
						</el-col>
						<el-col :span="6" class="cards">
							<div class="box">
								<div class="box_num">
									<div class="box_color">上架赠送数量</div>
									<div class="text_color">
										<span>{{youhuiquanData.sjzsCount ? youhuiquanData.sjzsCount : 0}}</span>
									</div>
								</div>
							</div>
						</el-col>
					</el-row>
				</el-tab-pane>
				<!-- <el-tab-pane label="自营商城统计" name="ziying">
					<el-row :gutter="12">
						<el-col :span="6" class="cards">
							<div class="box">
								<div class="box_num">
									<div class="box_color">总收入</div>
									<div class="text_color"><span>{{sumincome ? sumincome : 0}}</span>元</div>
								</div>
							</div>
						</el-col>
						<el-col :span="6" class="cards">
							<div class="box">
								<div class="box_num">
									<div class="box_color">当前收入</div>
									<div class="text_color"><span>{{income ? income : 0}}</span>元</div>
								</div>
							</div>
						</el-col>
						<el-col :span="6" class="cards">
							<div class="box">
								<div class="box_num">
									<div class="box_color">微信收入</div>
									<div class="text_color"><span>{{wxincome ? wxincome : 0}}</span>元</div>
								</div>
							</div>
						</el-col>
						<el-col :span="6" class="cards">
							<div class="box">
								<div class="box_num">
									<div class="box_color">支付宝收入</div>
									<div class="text_color"><span>{{zfbincome ? zfbincome : 0}}</span>元</div>
								</div>
							</div>
						</el-col>
					</el-row>
					<el-row :gutter="12">
						<el-col :span="6" class="cards">
							<div class="box">
								<div class="box_num">
									<div class="box_color">零钱收入</div>
									<div class="text_color"><span>{{yueicome ? yueicome : 0}}</span>元</div>
								</div>
							</div>
						</el-col>
						<el-col :span="6" class="cards">
							<div class="box">
								<div class="box_num">
									<div class="box_color">订单年成交量</div>
									<div class="text_color"><span>{{ordersum0 ? ordersum0 : 0}}</span>件</div>
								</div>
							</div>
						</el-col>
						<el-col :span="6" class="cards">
							<div class="box">
								<div class="box_num">
									<div class="box_color">订单月成交量</div>
									<div class="text_color"><span>{{ordersum1 ? ordersum1 : 0}}</span>件</div>
								</div>
							</div>
						</el-col>
						<el-col :span="6" class="cards">
							<div class="box">
								<div class="box_num">
									<div class="box_color">订单日成交量</div>
									<div class="text_color"><span>{{ordersum2 ? ordersum2 : 0}}</span>件</div>
								</div>
							</div>
						</el-col>
					</el-row>
				</el-tab-pane>
				<el-tab-pane label="动态统计" name="dongtai">
					<el-row :gutter="12">
						<el-col :span="6" class="cards">
							<div class="box">
								<div class="box_num">
									<div class="box_color">动态总数</div>
									<div class="text_color">
										<span>{{totalCount ? totalCount : 0}}</span>个
									</div>
								</div>
							</div>
						</el-col>
						<el-col :span="6" class="cards">
							<div class="box">
								<div class="box_num">
									<div class="box_color">待审核数量</div>
									<div class="text_color">
										<span>{{auditIng ? auditIng : 0}}</span>个
									</div>
								</div>
							</div>
						</el-col>
						<el-col :span="6" class="cards">
							<div class="box">
								<div class="box_num">
									<div class="box_color">已发布数量</div>
									<div class="text_color">
										<span>{{auditEd ? auditEd : 0}}</span>个
									</div>
								</div>
							</div>
						</el-col>
						<el-col :span="6" class="cards">
							<div class="box">
								<div class="box_num">
									<div class="box_color">已下架数量</div>
									<div class="text_color">
										<span>{{auditOut ? auditOut : 0}}</span>个
									</div>
								</div>
							</div>
						</el-col>
					</el-row>
				</el-tab-pane> -->
				<!-- <el-tab-pane label="核销统计" name="hexiao">
					<el-row :gutter="12">
						<el-col :span="6" class="cards">
							<div class="box">
								<div class="box_num">
									<div class="box_color">美团核销总数</div>
									<div class="text_color">
										<span>{{hexiaoCount.mtCount ? hexiaoCount.mtCount : 0}}</span>个
									</div>
								</div>
							</div>
						</el-col>
						<el-col :span="6" class="cards">
							<div class="box">
								<div class="box_num">
									<div class="box_color">美团核销金额</div>
									<div class="text_color">
										<span>{{hexiaoCount.mtMoney ?  Number(hexiaoCount.mtMoney).toFixed(2) : 0}}</span>元
									</div>
								</div>
							</div>
						</el-col>
						<el-col :span="6" class="cards">
							<div class="box">
								<div class="box_num">
									<div class="box_color">抖音核销总数</div>
									<div class="text_color">
										<span>{{hexiaoCount.dyCount ? hexiaoCount.dyCount : 0}}</span>个
									</div>
								</div>
							</div>
						</el-col>
						<el-col :span="6" class="cards">
							<div class="box">
								<div class="box_num">
									<div class="box_color">抖音核销金额</div>
									<div class="text_color">
										<span>{{hexiaoCount.dyMoney ? Number(hexiaoCount.dyMoney).toFixed(2): 0}}</span>元
									</div>
								</div>
							</div>
						</el-col>
						<el-col :span="6" class="cards">
							<div class="box">
								<div class="box_num">
									<div class="box_color">快手核销总数</div>
									<div class="text_color">
										<span>{{hexiaoCount.ksCount ? hexiaoCount.ksCount : 0}}</span>个
									</div>
								</div>
							</div>
						</el-col>
						<el-col :span="6" class="cards">
							<div class="box">
								<div class="box_num">
									<div class="box_color">快手核销金额</div>
									<div class="text_color">
										<span>{{hexiaoCount.ksMoney ? Number(hexiaoCount.ksMoney).toFixed(2): 0}}</span>元
									</div>
								</div>
							</div>
						</el-col>
						<el-col :span="6" class="cards">
							<div class="box">
								<div class="box_num">
									<div class="box_color">购买核销总数</div>
									<div class="text_color">
										<span>{{hexiaoCount.payCount ? hexiaoCount.payCount : 0}}</span>个
									</div>
								</div>
							</div>
						</el-col>
						<el-col :span="6" class="cards">
							<div class="box">
								<div class="box_num">
									<div class="box_color">购买核销金额</div>
									<div class="text_color">
										<span>{{hexiaoCount.payMoney ?  Number(hexiaoCount.payMoney).toFixed(2) : 0}}</span>元
									</div>
								</div>
							</div>
						</el-col>
						<el-col :span="6" class="cards">
							<div class="box">
								<div class="box_num">
									<div class="box_color">赠送核销总数</div>
									<div class="text_color">
										<span>{{hexiaoCount.zsCount ? hexiaoCount.zsCount : 0}}</span>个
									</div>
								</div>
							</div>
						</el-col>
						<el-col :span="6" class="cards">
							<div class="box">
								<div class="box_num">
									<div class="box_color">赠送核销金额</div>
									<div class="text_color">
										<span>{{hexiaoCount.zsMoney ? Number(hexiaoCount.zsMoney).toFixed(2): 0}}</span>元
									</div>
								</div>
							</div>
						</el-col>
					</el-row>
				</el-tab-pane> -->
				<el-tab-pane label="待审核统计" name="daishenhe">
					<el-row :gutter="12">
						<!-- <el-col :span="6" class="cards">
							<div class="box">
								<div class="box_num">
									<div class="box_color">活动待审核</div>
									<div class="text_color"><span style="color: red;cursor: pointer;"
											@click="btnDsh(1)">{{chatNumHd ? chatNumHd : 0 }}</span>个</div>
								</div>
							</div>
						</el-col>
						<el-col :span="6" class="cards">
							<div class="box">
								<div class="box_num">
									<div class="box_color">认证待审核</div>
									<div class="text_color"><span style="color: red;cursor: pointer;"
											@click="btnDsh(2)">{{chatNumRz ? chatNumRz : 0}}</span>个</div>
								</div>
							</div>
						</el-col>
						<el-col :span="6" class="cards">
							<div class="box">
								<div class="box_num">
									<div class="box_color">动态待审核</div>
									<div class="text_color"><span style="color: red;cursor: pointer;"
											@click="btnDsh(6)">{{chatNumDt ? chatNumDt : 0}}</span>个</div>
								</div>
							</div>
						</el-col> -->
						<el-col :span="6" class="cards">
							<div class="box">
								<div class="box_num">
									<div class="box_color">提现待审核</div>
									<div class="text_color"><span style="color: red;cursor: pointer;"
											@click="btnDsh(7)">{{chatNumTx ? chatNumTx : 0}}</span>个</div>
								</div>
							</div>
						</el-col>
						<el-col :span="6" class="cards">
							<div class="box">
								<div class="box_num">
									<div class="box_color">注销待审核</div>
									<div class="text_color"><span style="color: red;cursor: pointer;"
											@click="btnDsh(9)">{{chatNumZx ? chatNumZx : 0}}</span>个</div>
								</div>
							</div>
						</el-col>
						<el-col :span="6" class="cards">
							<div class="box">
								<div class="box_num">
									<div class="box_color">投诉待审核</div>
									<div class="text_color"><span style="color: red;cursor: pointer;"
											@click="btnDsh(3)">{{chatNumTs ? chatNumTs : 0}}</span>个</div>
								</div>
							</div>
						</el-col>
						<!-- <el-col :span="6" class="cards">
							<div class="box">
								<div class="box_num">
									<div class="box_color">自营订单待处理</div>
									<div class="text_color"><span style="color: red;cursor: pointer;"
											@click="btnDsh(8)">{{chatNumZydd ? chatNumZydd : 0}}</span>个</div>
								</div>
							</div>
						</el-col> -->
					</el-row>
				</el-tab-pane>

			</el-tabs>
		</div>

		<div class="home-page__charts-grid">
			<div class="home-page__chart-card">
				<div class="home-page__chart-head">
					<span class="home-page__chart-title">订单折线图</span>
					<el-date-picker
						v-model="selectYear"
						:picker-options="pickerOptions"
						type="daterange"
						range-separator="至"
						start-placeholder="开始日期"
						end-placeholder="结束日期"
						@change="selectYears">
					</el-date-picker>
				</div>
				<div id="Chart1" class="home-page__chart-body" v-loading="tableDataLoading1"></div>
			</div>
			<div class="home-page__chart-card">
				<div class="home-page__chart-head">
					<span class="home-page__chart-title">用户增长折线图</span>
					<el-date-picker
						v-model="selectDayDate"
						:picker-options="pickerOptions"
						type="daterange"
						range-separator="至"
						start-placeholder="开始日期"
						end-placeholder="结束日期"
						@change="selectTime">
					</el-date-picker>
				</div>
				<div id="Chart2" class="home-page__chart-body" v-loading="tableDataLoading2"></div>
			</div>
		</div>
	</div>
</template>

<script>
	//按需加载echarts
	import * as echarts from 'echarts/core';
	import {
		ToolboxComponent,
		TooltipComponent,
		GridComponent,
		LegendComponent
	} from 'echarts/components';
	import {
		LineChart
	} from 'echarts/charts';
	import {
		UniversalTransition
	} from 'echarts/features';
	import {
		CanvasRenderer
	} from 'echarts/renderers';
	export default {
		data() {
			return {
				limit: 5,
				page: 1,
				tableData: {},
				tableData2: {
					list: [],
					totalCount: 0
				},
				colonelData: [],
				commodityData: [],
				userjfData: [],
				incomeData: [],
				activeName: 'first',
				tableDataLoading: true,
				flag: 1,
				usersumData: {},
				taskStat: {},
				data: '',
				time: '',
				platform: '',
				type: '',
				balance: -1,
				flag: 1,
				info: {
					stockDate: this.getNowTime(), //日期
				},
				flags: [{
					value: 1,
					label: '按天查询'
				}, {
					value: 2,
					label: '按月查询'
				}, {
					value: 3,
					label: '按年查询'
				}],
				userStat: {
					vipList: [],
				},
				phoneStat: 0,
				notphoneStat: 0, //未绑定手机号
				appStat: 0,
				weixinStat: 0,
				mpStat: 0,
				wapStat: 0,
				rechgeData: {},
				shouyiData: {},
				orderData: {},
				youhuiquanData: {},
				shouyiOrData: {},
				// 图表----------
				tableDataLoading1: true,
				tableDataLoading2: true,
				orderEcharts: {}, //订单折线图
				increaseEcharts: {}, //用户增长折线图
				selectDayDate: '',
				pickerOptions: {
					onPick: ({
						maxDate,
						minDate
					}) => {
						// 把选择的第一个日期赋值给一个变量。
						this.choiceDate = minDate.getTime()
						// 如何你选择了两个日期了，就把那个变量置空
						if (maxDate) this.choiceDate = ''
					},
					disabledDate: time => {
						// 如何选择了一个日期
						if (this.choiceDate) {
							// 7天的时间戳
							const one = 6 * 24 * 3600 * 1000
							// 当前日期 - one = 7天之前
							const minTime = this.choiceDate - one
							// 当前日期 + one = 7天之后
							const maxTime = this.choiceDate + one
							return (
								time.getTime() < minTime ||
								time.getTime() > maxTime
							)
						} else {
							return false
						}
					}
				},
				bodyStyle: {
					padding: '0'
				},
				selectYear: '',
				qiehuan_type: 1,
				lirunStat: {},
				// ============
				sumincome: '',
				income: '',
				wxincome: '',
				zfbincome: '',
				yueicome: '',
				ordersum0: '',
				ordersum1: '',
				ordersum2: '',
				refundMoney: '',
				refundIncome: '',
				refundNum: '',
				pingMoney: '',
				shopSumincome: {},
				sumincomeTg: '',
				incomeTg: '',
				wxincomeTg: '',
				zfbincomeTg: '',
				yueicomeTg: '',
				ordersum0Tg: '',
				ordersum1Tg: '',
				ordersum2Tg: '',
				fenxiaoStat: {},

				chatNumZx: 0,
				chatNumTs: 0,
				chatNumRz: 0,
				chatNumTx: 0,
				chatNumZydd: 0,
				chatNumDt: 0,
				chatNumHd: 0,
				totalEdMoney: 0,
				totalMoney: 0,
				totalIngMoney: 0,
				auditEd: 0,
				auditIng: 0,
				auditOut: 0,
				totalCount: 0,
				disanfangData: {},
				hexiaoCount: {},
				tableDataAx: {},
				classify:1,
			}
		},
		mounted() {

			this.userData()
			this._getData() //用户统计
			// this.incomeSelect()//收入

			this.getUserOrderEcharts();
			this.getUserIncreaseEcharts()
			setInterval(() => {
				if (this.$cookie.get('numberZx')) {
					this.chatNumZx = this.$cookie.get('numberZx')
				}
				if (this.$cookie.get('numberTs')) {
					this.chatNumTs = this.$cookie.get('numberTs')
				}

				if (this.$cookie.get('numberRz')) {
					this.chatNumRz = this.$cookie.get('numberRz')
				}
				if (this.$cookie.get('numberTx')) {
					this.chatNumTx = this.$cookie.get('numberTx')
				}
				if (this.$cookie.get('numberZyDfh') || this.$cookie.get('numberZyDtk')) {
					this.chatNumZydd = Number(this.$cookie.get('numberZyDfh')) + Number(this.$cookie.get(
						'numberZyDtk'))
				}
				if (this.$cookie.get('numberDt')) {
					this.chatNumDt = this.$cookie.get('numberDt')
				}
				if (this.$cookie.get('numberHd')) {
					this.chatNumHd = this.$cookie.get('numberHd')
				}

			}, 1000)


		},
		methods: {
			// 详情跳转
			updates(row) {
				this.$router.push({
					path: '/userDetail',
					query: {
						userId: row.userId
					}
				})
			},
			updates1(userId) {
				this.$router.push({
					path: '/userDetail',
					query: {
						userId: userId
					}
				})
			},
			updates2(row) {
				this.$router.push({
					path: '/userDetail',
					query: {
						userId: row.userId2
					}
				})
			},
			handleSizeChange(val) {
				this.limit = val;
				this.colonel();
			},
			handleCurrentChange(val) {
				this.page = val;
				this.colonel();
			},
			handleSizeChangeT(val) {
				this.limit = val;
				this.aixinqianbaoSelect();
			},
			handleCurrentChangeT(val) {
				this.page = val;
				this.aixinqianbaoSelect();
			},
			handleSizeChangeC(val) {
				this.limit = val;
				this.taskData();
			},
			handleCurrentChangeC(val) {
				this.page = val;
				this.taskData();
			},
			// 用户分析选择日期
			animeDat() {
				this._getData()
			},
			// 订单分析选择日期
			animeOrder() {
				this.taskData()
				this.colonel()
				this.userData()
				this.rechSelectZf()
				this.shouyiSelect()
				this.rechSelectDd()
				this.youhuiquanSelect()
				this.moneySelect()
				this.orderSelect()
				this.dongtaiSelectTg()
				this.dongtaiSelectHx()
				this.shouhuiquanOrderSelect()
				this.aixinqianbaoSelect()
			},
			// 订单分析年月日
			orderfenxi(value) {
				let vanumber = value
				if (vanumber === 1) {
					this.flag = 1
					this.taskData()
					this.colonel()
					this.userData()
					this.rechSelectZf()
					this.shouyiSelect()
					this.rechSelectDd()
					this.youhuiquanSelect()
					this.moneySelect()
					this.orderSelect()
					this.dongtaiSelectTg()
					this.dongtaiSelectHx()
					this.shouhuiquanOrderSelect()
					this.aixinqianbaoSelect()
				}
				if (vanumber === 2) {
					this.flag = 2
					this.taskData()
					this.colonel()
					this.userData()
					this.rechSelectZf()
					this.shouyiSelect()
					this.rechSelectDd()
					this.youhuiquanSelect()
					this.moneySelect()
					this.orderSelect()
					this.dongtaiSelectTg()
					this.dongtaiSelectHx()
					this.shouhuiquanOrderSelect()
					this.aixinqianbaoSelect()
				}
				if (vanumber === 3) {
					this.flag = 3
					this.taskData()
					this.colonel()
					this.userData()
					this.rechSelectZf()
					this.shouyiSelect()
					this.rechSelectDd()
					this.youhuiquanSelect()
					this.moneySelect()
					this.orderSelect()
					this.dongtaiSelectTg()
					this.dongtaiSelectHx()
					this.shouhuiquanOrderSelect()
					this.aixinqianbaoSelect()
				}
			},
			// Tabs点击事件
			handleClick(tab, event) {
				if (tab._props.label == '充电桩分析') {
					this.taskData()
				}
				if (tab._props.label == '收益统计') {
					this.shouyiSelect()
				}
				if (tab._props.label == '用户积分分析') {
					this.colonel()
				}
				if (tab._props.label == '用户分析') {
					this.info.stockDate = this.getNowTime()
					this.userData()
				}
				if (tab._props.label == '支付统计') {
					this.flag = 1
					this.rechSelectZf()
				}
				if (tab._props.label == '台球麻将ps系列订单统计') {
					this.flag = 1
					this.classify = 1
					this.rechSelectDd()
				}
				if (tab._props.label == '租柜订单统计') {
					this.flag = 1
					this.classify = 2
					this.rechSelectDd()
				}
				if (tab._props.label == '优惠券统计') {
					this.youhuiquanSelect()
				}
				if (tab._props.label == '自营商城统计') {
					this.info.stockDate = this.getNowTime()
					this.moneySelect()
					this.orderSelect()
				}
				if (tab._props.label == '动态统计') {
					this.info.stockDate = this.getNowTime()
					this.dongtaiSelectTg()
				}
				if (tab._props.label == '核销统计') {
					this.info.stockDate = this.getNowTime()
					this.dongtaiSelectHx()
				}
				if (tab._props.label == '公益订单统计') {
					this.shouhuiquanOrderSelect()
				}
				if (tab._props.label == '爱心钱包统计') {
					this.aixinqianbaoSelect()
				}


			},
			//处理默认选中当前日期
			getNowTime() {
				var now = new Date();
				var year = now.getFullYear(); //得到年份
				var month = now.getMonth(); //得到月份
				var date = now.getDate(); //得到日期
				var hh = now.getHours() < 10 ? "0" + now.getHours() : now.getHours();
				var mm = now.getMinutes() < 10 ? "0" + now.getMinutes() : now.getMinutes();
				var ss = now.getSeconds() < 10 ? "0" + now.getSeconds() : now.getSeconds();
				month = month + 1;
				month = month.toString().padStart(2, "0");
				date = date.toString().padStart(2, "0");
				var defaultDate = `${year}-${month}-${date}`;
				return defaultDate;
				this.$set(this.info, "stockDate", defaultDate);
			},
			// 用户统计
			_getData() {
				this.$http({
					url: this.$http.adornUrl('user/homeMessage'),
					method: 'get',
					params: this.$http.adornParams({})
				}).then(({
					data
				}) => {
					if (data.code == 0) {
						let returnData = data.data;
						this.tableData = returnData
					}
				})
			},
			// 收入统计
			//  incomeSelect(){
			//  this.$http({
			// url: this.$http.adornUrl('statistical/income'),
			// method: 'get',
			// params: this.$http.adornParams({
			// })
			//  }).then(({data}) => {
			//   if(data.code == 0){
			// 	  let returnData = data.data;
			// 	  this.incomeData = returnData
			//   }
			//  })
			//  },
			//充电桩分析
			taskData() {
				this.tableDataLoading = true
				this.$http({
					url: this.$http.adornUrl('orders/selectTearoomOrdersCount'),
					method: 'get',
					params: this.$http.adornParams({
						'time': this.info.stockDate,
						'flag': this.flag,
						'page': this.page,
						'limit': this.limit,
					})
				}).then(({
					data
				}) => {
					this.tableDataLoading = false
					let returnData = data.data;
					this.taskStat = returnData
					
				})
			},
			// 用户积分分析
			colonel() {
				this.tableDataLoading = true
				this.$http({
					url: this.$http.adornUrl('integral/selectUserIntegralDetailsCount'),
					method: 'get',
					params: this.$http.adornParams({
						'page': this.page,
						'limit': this.limit,
						'time': this.info.stockDate,
						'flag': this.flag,
					})
				}).then(({
					data
				}) => {
					this.tableDataLoading = false
					if (data.code == 0) {
						let returnData = data.data
						this.tableData2 = returnData
					}
				})
			},
			// 用户分析
			userData() {
				this.$http({
					url: this.$http.adornUrl('user/userPlatform'),
					method: 'get',
					params: this.$http.adornParams({
						'data': this.info.stockDate,
						'type': this.flag,
					})
				}).then(({
					data
				}) => {
					let returnData = data.data;
					this.userStat = returnData
				})
			},
			// 充值统计
			// rechSelect() {
			// 	this.$http({
			// 		url: this.$http.adornUrl('cash/payMember'),
			// 		method: 'get',
			// 		params: this.$http.adornParams({
			// 			'time': this.info.stockDate,
			// 			'flag': this.flag
			// 		})
			// 	}).then(({
			// 		data
			// 	}) => {
			// 		if (data.code == 0) {
			// 			let returnData = data.data
			// 			this.rechgeData = returnData
			// 		}
			// 	})
			// },
			// 收益统计 
			shouyiSelect() {
				this.tableDataLoading = true
				this.$http({
					url: this.$http.adornUrl('orders/selectPayMoneyData'),
					method: 'get',
					params: this.$http.adornParams({
						'time': this.info.stockDate,
						'type': this.flag,
					})
				}).then(({
					data
				}) => {
					if (data.code == 0) {
						this.tableDataLoading = false
						let returnData = data.data
						this.shouyiData = returnData
					}
				})
			},
			// 优惠券统计
			youhuiquanSelect() {
				this.tableDataLoading = true
				this.$http({
					url: this.$http.adornUrl('selfCouponUser/selectCouponData'),
					method: 'get',
					params: this.$http.adornParams({
						'time': this.info.stockDate,
						'type': this.flag,
					})
				}).then(({
					data
				}) => {
					if (data.status == 0) {
						this.tableDataLoading = false
						let returnData = data.data
						this.youhuiquanData = returnData
					}
				})
			},

			// 支付统计
			rechSelectZf() {
				this.$http({
					url: this.$http.adornUrl('cash/payMoney'),
					method: 'get',
					params: this.$http.adornParams({
						'time': this.info.stockDate,
						'flag': this.flag
					})
				}).then(({
					data
				}) => {
					if (data.code == 0) {
						let returnData = data.data
						this.rechgeData = returnData
					}
				})
			},
			// 订单统计
			rechSelectDd() {
				this.$http({
					url: this.$http.adornUrl('orders/selectTearoomCount'),
					method: 'get',
					params: this.$http.adornParams({
						'time': this.info.stockDate,
						'flag': this.flag,
						'classify':this.classify,
					})
				}).then(({
					data
				}) => {
					if (data.code == 0) {
						let returnData = data.data
						this.orderData = returnData
					}
				})
			},
			//订单折线图
			getUserOrderEcharts(startTime, endTime) {
				this.tableDataLoading1 = true
				var times = this.info.stockDate.slice(0, 10)
				if (startTime) {
					var startTime = startTime
					var endTime = endTime
				} else {
					// var now = Date.parse(new Date())
					// var oldD = new Date(now)
					// // var year = oldD.getFullYear(); //得到年份
					// var year = this.info2.stockDate
					// // this.selectYear = year
					var now = Date.parse(new Date())
					// var choiceDate = (new Date(now)).getTime() / 1000;
					var choiceDate = Date.parse(new Date())
					var one = 6 * 24 * 3600 * 1000
					// 当前日期 - one = 7天之前
					var minTime = choiceDate - one
					var oldD = new Date(minTime)
					var year = oldD.getFullYear(); //得到年份
					var month = oldD.getMonth(); //得到月份
					var date = oldD.getDate(); //得到日期
					var hh = oldD.getHours() < 10 ? "0" + oldD.getHours() : oldD.getHours();
					var mm = oldD.getMinutes() < 10 ? "0" + oldD.getMinutes() : oldD.getMinutes();
					var ss = oldD.getSeconds() < 10 ? "0" + oldD.getSeconds() : oldD.getSeconds();
					month = month + 1;
					month = month.toString().padStart(2, "0");
					date = date.toString().padStart(2, "0");
					var defaultDate = `${year}-${month}-${date}`;

					var startTime = defaultDate
					var endTime = times
					this.selectYear = [startTime, endTime]
				}

				this.$http({
					url: this.$http.adornUrl('orders/selectOrdersCountStatisticsByYear'),
					method: 'get',
					params: this.$http.adornParams({
						// 'time': year ? year : '',
						'startTime': startTime ? startTime : '',
						'endTime': endTime ? endTime : ''
					})
				}).then(({
					data
				}) => {
					if (data && data.code == 0) {
						this.orderEcharts = data.data
						//使用数据初始化图表
						this.setEcharts()
						this.tableDataLoading1 = false
					}
					// this.dataInfo = data.data
				})
			},
			//用户增长折线图
			getUserIncreaseEcharts(startTime, endTime) {
				this.tableDataLoading2 = true
				var times = this.info.stockDate.slice(0, 10)
				console.log('times', times)
				if (startTime) {
					var startTime = startTime
					var endTime = endTime
				} else {
					var now = Date.parse(new Date())
					// var choiceDate = (new Date(now)).getTime() / 1000;
					var choiceDate = Date.parse(new Date())
					var one = 6 * 24 * 3600 * 1000
					// 当前日期 - one = 7天之前
					var minTime = choiceDate - one
					var oldD = new Date(minTime)
					var year = oldD.getFullYear(); //得到年份
					var month = oldD.getMonth(); //得到月份
					var date = oldD.getDate(); //得到日期
					var hh = oldD.getHours() < 10 ? "0" + oldD.getHours() : oldD.getHours();
					var mm = oldD.getMinutes() < 10 ? "0" + oldD.getMinutes() : oldD.getMinutes();
					var ss = oldD.getSeconds() < 10 ? "0" + oldD.getSeconds() : oldD.getSeconds();
					month = month + 1;
					month = month.toString().padStart(2, "0");
					date = date.toString().padStart(2, "0");
					var defaultDate = `${year}-${month}-${date}`;

					var startTime = defaultDate
					var endTime = times
					this.selectDayDate = [startTime, endTime]
				}
				this.$http({
					url: this.$http.adornUrl('user/selectUserCountStatisticsByTime'),
					method: 'get',
					params: this.$http.adornParams({
						'startTime': startTime ? startTime : '',
						'endTime': endTime ? endTime : ''
					})
				}).then(({
					data
				}) => {
					if (data && data.code == 0) {
						this.increaseEcharts = data.data
						//使用数据初始化图表
						this.setEcharts2()
						this.tableDataLoading2 = false
					}
					// this.dataInfo = data.data
				})
			},
			//使用数据初始化图表
			setEcharts() {
				echarts.use([
					ToolboxComponent,
					TooltipComponent,
					GridComponent,
					LegendComponent,
					LineChart,
					CanvasRenderer,
					UniversalTransition
				]);

				var chartDom = document.getElementById('Chart1');
				var myChart = echarts.init(chartDom);
				var option;

				option = {
					tooltip: {
						trigger: 'axis'
					},
					legend: {
						data: ['订单总量', '待支付单量', '待使用订单', '使用中订单', '已完成订单', '已取消订单', '待评价订单'],
						top: '5%'
					},
					grid: {
						left: '3%',
						right: '4%',
						bottom: '3%',
						containLabel: true
					},
					toolbox: {
						top: '3%',
						feature: {
							saveAsImage: {},

						}
					},
					xAxis: {
						type: 'category',
						boundaryGap: false,
						data: this.orderEcharts.year
					},
					yAxis: {
						type: 'value'
					},
					series: [{
							name: '订单总量',
							type: 'line',
							stack: 'Total',
							data: this.orderEcharts.ordersCountList
						},
						{
							name: '待支付单量',
							type: 'line',
							stack: 'Total',
							data: this.orderEcharts.ordersDzfCountList
						},
						{
							name: '待使用订单',
							type: 'line',
							stack: 'Total',
							data: this.orderEcharts.ordersDsyCountList
						},
						{
							name: '使用中订单',
							type: 'line',
							stack: 'Total',
							data: this.orderEcharts.ordersSyzCountList
						},
						{
							name: '已完成订单',
							type: 'line',
							stack: 'Total',
							data: this.orderEcharts.ordersYwcCountList
						},
						{
							name: '已取消订单',
							type: 'line',
							stack: 'Total',
							data: this.orderEcharts.ordersYqxCountList
						},
						{
							name: '待评价订单',
							type: 'line',
							stack: 'Total',
							data: this.orderEcharts.ordersDpjCountList
						},

					]
				};

				option && myChart.setOption(option);
			},
			//使用数据初始化图表
			setEcharts2() {
				echarts.use([
					ToolboxComponent,
					TooltipComponent,
					GridComponent,
					LegendComponent,
					LineChart,
					CanvasRenderer,
					UniversalTransition
				]);
				var chartDom = document.getElementById('Chart2');
				var myChart = echarts.init(chartDom);
				var option;

				option = {
					tooltip: {
						trigger: 'axis'
					},
					legend: {
						data: ['用户增长量'],
						top: '5%'
					},
					grid: {
						left: '3%',
						right: '4%',
						bottom: '3%',
						containLabel: true
					},
					toolbox: {
						top: '3%',
						feature: {
							saveAsImage: {},

						}
					},
					xAxis: {
						type: 'category',
						boundaryGap: false,
						data: this.increaseEcharts.year
					},
					yAxis: {
						type: 'value'
					},
					series: [{
						name: '用户增长量',
						type: 'line',
						stack: 'Total',
						data: this.increaseEcharts.userCountList
					}, ]
				};

				option && myChart.setOption(option);
			},
			//根据年份筛选
			selectYears(e) {
				console.log(e, '`````````', this.info.stockDate)
				if (e == null) {
					this.getUserOrderEcharts()
				} else {
					// var now = new Date();
					// // var year = now.getFullYear(); //得到年份
					// var year = this.info2.stockDate
					// let startTime = (new Date(e)).getTime() / 1000;
					// this.getUserOrderEcharts(year)
					var now = new Date(e[0]);
					var year = now.getFullYear(); //得到年份
					var month = now.getMonth(); //得到月份
					var date = now.getDate(); //得到日期
					var hh = now.getHours() < 10 ? "0" + now.getHours() : now.getHours();
					var mm = now.getMinutes() < 10 ? "0" + now.getMinutes() : now.getMinutes();
					var ss = now.getSeconds() < 10 ? "0" + now.getSeconds() : now.getSeconds();
					month = month + 1;
					month = month.toString().padStart(2, "0");
					date = date.toString().padStart(2, "0");
					// var defaultDate = `${year}-${month}-${date} ${hh}:${mm}:${ss}`;
					var defaultDate = `${year}-${month}-${date}`;

					var now1 = new Date(e[1]);
					var year1 = now1.getFullYear(); //得到年份
					var month1 = now1.getMonth(); //得到月份
					var date1 = now1.getDate(); //得到日期
					var hh1 = now1.getHours() < 10 ? "0" + now1.getHours() : now1.getHours();
					var mm1 = now1.getMinutes() < 10 ? "0" + now1.getMinutes() : now1.getMinutes();
					var ss1 = now1.getSeconds() < 10 ? "0" + now1.getSeconds() : now1.getSeconds();
					month1 = month + 1;
					month1 = month.toString().padStart(2, "0");
					date1 = date1.toString().padStart(2, "0");
					var defaultDate1 = `${year1}-${month1}-${date1}`;

					let startTime = defaultDate
					let endTime = defaultDate1
					console.log(startTime, '`````````', endTime)
					this.getUserOrderEcharts(startTime, endTime)
				}
			},
			//根据日期区间筛选
			selectTime(e) {
				console.log(e)
				if (e == null) {
					this.getUserIncreaseEcharts()
				} else {
					var now = new Date(e[0]);
					var year = now.getFullYear(); //得到年份
					var month = now.getMonth(); //得到月份
					var date = now.getDate(); //得到日期
					var hh = now.getHours() < 10 ? "0" + now.getHours() : now.getHours();
					var mm = now.getMinutes() < 10 ? "0" + now.getMinutes() : now.getMinutes();
					var ss = now.getSeconds() < 10 ? "0" + now.getSeconds() : now.getSeconds();
					month = month + 1;
					month = month.toString().padStart(2, "0");
					date = date.toString().padStart(2, "0");
					// var defaultDate = `${year}-${month}-${date} ${hh}:${mm}:${ss}`;
					var defaultDate = `${year}-${month}-${date}`;

					var now1 = new Date(e[1]);
					var year1 = now1.getFullYear(); //得到年份
					var month1 = now1.getMonth(); //得到月份
					var date1 = now1.getDate(); //得到日期
					var hh1 = now1.getHours() < 10 ? "0" + now1.getHours() : now1.getHours();
					var mm1 = now1.getMinutes() < 10 ? "0" + now1.getMinutes() : now1.getMinutes();
					var ss1 = now1.getSeconds() < 10 ? "0" + now1.getSeconds() : now1.getSeconds();
					month1 = month + 1;
					month1 = month.toString().padStart(2, "0");
					date1 = date1.toString().padStart(2, "0");
					var defaultDate1 = `${year1}-${month1}-${date1}`;

					let startTime = defaultDate
					let endTime = defaultDate1

					// let startTime = (new Date(e[0])).getTime() / 1000;
					// let endTime = (new Date(e[1])).getTime() / 1000;
					this.getUserIncreaseEcharts(startTime, endTime)
				}
			},
			// --------------------------------
			// 收入金额统计
			moneySelect() {
				let page = this.page - 1
				var flagA = 3
				if (this.flag == 1) {
					flagA = 3
				}
				if (this.flag == 2) {
					flagA = 2
				}
				if (this.flag == 3) {
					flagA = 1
				}
				this.tableDataLoading = true
				this.$http({
					url: this.$http.adornUrl('shopOrders/income'),
					method: 'get',
					params: this.$http.adornParams({
						'data': this.info.stockDate,
						'way': flagA,
					})
				}).then(({
					data
				}) => {
					this.tableDataLoading = false
					let returnData = data.data;
					this.sumincome = returnData.sum0
					this.income = returnData.sum1
					this.wxincome = returnData.pay1
					this.zfbincome = returnData.pay2
					this.yueicome = returnData.pay3
				})
			},
			// 订单成交量统计
			orderSelect() {
				let page = this.page - 1
				this.tableDataLoading = true
				this.$http({
					url: this.$http.adornUrl('shopOrders/statistical'),
					method: 'get',
					params: this.$http.adornParams({
						'data': this.info.stockDate,
					})
				}).then(({
					data
				}) => {
					this.tableDataLoading = false
					let returnData = data.data;
					this.ordersum0 = returnData.sum0
					this.ordersum1 = returnData.sum1
					this.ordersum2 = returnData.sum2

				})
			},
			// 待审核跳转
			btnDsh(index) {
				if (index == 1) {
					this.$router.replace({
						name: 'locality',
						query: {
							status: 7
						}
					})
				}
				if (index == 2) {
					this.$router.replace({
						name: 'autonym',
						query: {
							status: 0
						}
					})
				}
				if (index == 3) {
					this.$router.replace({
						name: 'message',
						query: {
							status: 0
						}
					})
				}
				// if (index == 4) {
				// 	this.$router.replace({
				// 		name: 'groupChat',
				// 		query: {
				// 			status: 0
				// 		}
				// 	})
				// }
				// if (index == 5) {
				// 	this.$router.replace({
				// 		name: 'shopAdminTg',
				// 		query: {
				// 			status: 3
				// 		}
				// 	})
				// }
				if (index == 6) {
					this.$router.replace({
						name: 'dynamic',
						query: {
							status: 1
						}
					})
				}

				if (index == 7) {
					this.$router.replace({
						name: 'financeList',
						query: {
							status: 0
						}
					})
				}
				if (index == 8) {
					this.$router.replace({
						name: 'disposeOrderZy',
						query: {
							status: 0
						}
					})
				}
				if (index == 9) {
					this.$router.replace({
						name: 'messageZx',
						query: {
							status: 0
						}
					})
				}
			},
			// 动态统计
			dongtaiSelectTg() {
				var flagA = 1
				if (this.flag == 1) {
					flagA = 1
				}
				if (this.flag == 2) {
					flagA = 2
				}
				if (this.flag == 3) {
					flagA = 3
				}
				this.tableDataLoading = true
				this.$http({
					url: this.$http.adornUrl('gambit/talkMap'),
					method: 'post',
					data: this.$http.adornData({
						'date': this.info.stockDate,
						'way': flagA
					})
				}).then(({
					data
				}) => {
					this.tableDataLoading = false
					let returnData = data.data;
					this.totalCount = returnData.totalCount
					this.auditEd = returnData.auditEd
					this.auditIng = returnData.auditIng
					this.auditOut = returnData.auditOut
				})
			},
			// 核销统计
			dongtaiSelectHx() {
				var flagA = 1
				if (this.flag == 1) {
					flagA = 1
				}
				if (this.flag == 2) {
					flagA = 2
				}
				if (this.flag == 3) {
					flagA = 3
				}
				this.tableDataLoading = true
				this.$http({
					url: this.$http.adornUrl('selfCouponUser/selectTearoomCount'),
					method: 'get',
					params: this.$http.adornParams({
						'time': this.info.stockDate,
						'flag': flagA
					})
				}).then(({
					data
				}) => {
					this.tableDataLoading = false
					let returnData = data.data;
					this.hexiaoCount = returnData
				})
			},
			// 优惠券订单
			shouhuiquanOrderSelect() {
				this.$http({
					url: this.$http.adornUrl('orders/selectLoveOrdersData'),
					method: 'get',
					params: this.$http.adornParams({
						'time': this.info.stockDate,
						'type': this.flag,
					})
				}).then(({
					data
				}) => {
					if (data.code == 0) {
						let returnData = data.data
						this.shouyiOrData = returnData
					}
				})
			},
			// 爱心钱包统计
			aixinqianbaoSelect() {
				this.tableDataLoading = true
				this.$http({
					url: this.$http.adornUrl('welfareMoney/selectWelfareMoneyDetailsCountList'),
					method: 'get',
					params: this.$http.adornParams({
						'time': this.info.stockDate,
						'type': this.flag,
						'tearoomName': '',
						'page': this.page,
						'limit': this.limit,
					})
				}).then(({
					data
				}) => {
					this.tableDataLoading = false
					if (data.code == 0) {
						let returnData = data.data
						this.tableDataAx = returnData
					}
				})
			},
		}
	}
</script>

<style scoped>
.home-page__top {
	display: flex;
	gap: 12px;
	margin-bottom: 12px;
	align-items: stretch;
}

.home-page__hero {
	flex: 1;
	margin-bottom: 0;
}

.home-page__filter {
	width: 280px;
	flex-shrink: 0;
	margin-bottom: 0;
}

.home-page__filter-body {
	display: flex;
	flex-direction: column;
	gap: 12px;
}

.home-page__filter-select,
.home-page__filter-date {
	width: 100%;
}

.home-page__kpi-grid {
	display: grid;
	grid-template-columns: repeat(4, 1fr);
	gap: 12px;
	margin-bottom: 12px;
}

.home-page__kpi-card {
	display: flex;
	align-items: center;
	gap: 12px;
	padding: 16px;
	background: #fff;
	border: 1px solid #e8eaed;
	border-radius: 8px;
	box-shadow: 0 1px 4px rgba(0, 0, 0, 0.06);
}

.home-page__kpi-icon {
	width: 44px;
	height: 44px;
	border-radius: 8px;
	background: #ebf8ef;
	color: #5cc477;
	display: flex;
	align-items: center;
	justify-content: center;
	font-size: 20px;
	flex-shrink: 0;
}

.home-page__kpi-label {
	font-size: 13px;
	color: #909399;
	margin-bottom: 4px;
}

.home-page__kpi-value {
	font-size: 14px;
	color: #303133;
}

.home-page__kpi-value span {
	font-size: 22px;
	font-weight: 600;
	color: #5cc477;
	margin-right: 4px;
}

.home-page__detail-cols {
	display: grid;
	grid-template-columns: 1fr 1fr;
	gap: 12px;
	margin-bottom: 12px;
}

.home-page__detail-col {
	background: #fff;
	border: 1px solid #e8eaed;
	border-radius: 8px;
	padding: 16px;
	box-shadow: 0 1px 4px rgba(0, 0, 0, 0.06);
}

.home-page__detail-col-head {
	font-size: 14px;
	font-weight: 600;
	color: #1a2433;
	margin-bottom: 12px;
	padding-bottom: 8px;
	border-bottom: 1px solid #e8eaed;
}

.home-page__stat-tiles {
	display: grid;
	grid-template-columns: repeat(2, 1fr);
	gap: 12px;
}

.home-page__detail-col:last-child .home-page__stat-tiles {
	grid-template-columns: repeat(3, 1fr);
}

.home-page__stat-tile {
	padding: 12px;
	background: #f7f8fa;
	border-radius: 6px;
	border: 1px solid #e8eaed;
}

.home-page__stat-tile-label {
	font-size: 13px;
	color: #909399;
	margin-bottom: 6px;
}

.home-page__stat-tile-value {
	font-size: 14px;
	color: #303133;
}

.home-page__stat-tile-value span {
	font-size: 18px;
	font-weight: 600;
	color: #5cc477;
	margin-right: 4px;
}

.home-page__tabs-card {
	background: #fff;
	border: 1px solid #e8eaed;
	border-radius: 8px;
	padding: 16px;
	margin-bottom: 12px;
	box-shadow: 0 1px 4px rgba(0, 0, 0, 0.06);
}

.home-page__tabs {
	margin-bottom: 0;
}

.home-page .cards {
	margin-bottom: 12px;
}

.home-page .box {
	padding: 16px;
	border: 1px solid #e8eaed;
	border-radius: 6px;
	margin: 0;
	background: #f7f8fa;
	height: 100%;
	box-sizing: border-box;
}

.home-page .box_num .box_color {
	color: #909399;
	font-size: 13px;
	margin-bottom: 8px;
}

.home-page .text_color {
	color: #303133;
	font-size: 14px;
}

.home-page .text_color span {
	font-size: 18px;
	font-weight: 600;
	color: #5cc477;
	margin-right: 4px;
}

.home-page__pagination {
	display: flex;
	justify-content: flex-end;
	padding-top: 12px;
}

.home-page__table-wrap {
	margin: 0;
	padding: 0;
	border: none;
	box-shadow: none;
	background: transparent;
}

.home-page__chart-card {
	background: #fff;
	border: 1px solid #e8eaed;
	border-radius: 8px;
	padding: 16px;
	box-shadow: 0 1px 4px rgba(0, 0, 0, 0.06);
}

.home-page__charts-grid {
	display: grid;
	grid-template-columns: 1fr 1fr;
	gap: 12px;
}

.home-page__chart-head {
	display: flex;
	align-items: center;
	justify-content: space-between;
	margin-bottom: 12px;
	gap: 12px;
	flex-wrap: wrap;
}

.home-page__chart-title {
	font-size: 14px;
	font-weight: 600;
	color: #1a2433;
}

.home-page__chart-body {
	width: 100%;
	height: 300px;
}

.home-page__pending-link {
	color: #f56c6c;
	cursor: pointer;
}

.home-page__pending-link:hover {
	color: #e64242;
}

@media (max-width: 1200px) {
	.home-page__kpi-grid {
		grid-template-columns: repeat(2, 1fr);
	}

	.home-page__detail-cols,
	.home-page__charts-grid {
		grid-template-columns: 1fr;
	}

	.home-page__detail-col:last-child .home-page__stat-tiles {
		grid-template-columns: repeat(2, 1fr);
	}
}

@media (max-width: 768px) {
	.home-page__top {
		flex-direction: column;
	}

	.home-page__filter {
		width: 100%;
	}

	.home-page__kpi-grid {
		grid-template-columns: 1fr;
	}
}
</style>