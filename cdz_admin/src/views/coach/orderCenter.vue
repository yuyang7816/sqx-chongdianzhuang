<template>
	<div class="nf-page order-center-jl-page">
		<div class="nf-page-hero">
			<span class="nf-page-hero__badge">教练订单</span>
			<h2 class="nf-page-hero__title">教练订单列表</h2>
			<p class="nf-page-hero__desc">查询与管理教练上门/到店订单，支持多条件筛选、导出及完成、转单、退款等操作。</p>
			<div class="nf-page-hero__bar"></div>
		</div>

		<el-tabs v-model="activeName" @tab-click="handleClick" class="order-center-jl-page__tabs">
		<el-tab-pane label="订单列表" name="first">
			<div class="nf-filter-card">
				<div class="nf-filter-card__head">
					<i class="el-icon-search nf-filter-card__head-icon"></i>
					<span>筛选条件</span>
				</div>
				<el-form :inline="true" class="nf-filter-card__body" @submit.native.prevent>
					<el-form-item label="项目名称">
						<el-input class="order-center-jl-page__filter-input order-center-jl-page__filter-input--wide"
							@keydown.enter.native="select" placeholder="请输入项目名称" v-model="title"></el-input>
					</el-form-item>
					<el-form-item label="城市">
						<el-input class="order-center-jl-page__filter-input order-center-jl-page__filter-input--wide"
							@keydown.enter.native="select" placeholder="请输入地区" v-model="city"></el-input>
					</el-form-item>
					<el-form-item label="状态">
						<el-select class="order-center-jl-page__filter-select" v-model="status" placeholder="请选择状态"
							@change="animeDat(status)">
							<el-option v-for="item in statesnum" :key="item.value" :label="item.label" :value="item.value">
							</el-option>
						</el-select>
					</el-form-item>
					<el-form-item label="订单类型">
						<el-select class="order-center-jl-page__filter-select" v-model="ordersType" placeholder="请选择类型"
							@change="animeDat()">
							<el-option v-for="item in typeNum" :key="item.value" :label="item.label" :value="item.value">
							</el-option>
						</el-select>
					</el-form-item>
					<el-form-item label="是否加钟">
						<el-select class="order-center-jl-page__filter-select" v-model="statusJz" placeholder="请选择"
							@change="animeDat(status)">
							<el-option v-for="item in statesnumJz" :key="item.value" :label="item.label" :value="item.value">
							</el-option>
						</el-select>
					</el-form-item>
					<el-form-item label="是否超时">
						<el-select class="order-center-jl-page__filter-select" v-model="overTimeOrders" placeholder="请选择"
							@change="animeDat()">
							<el-option v-for="item in statesnumCs" :key="item.value" :label="item.label" :value="item.value">
							</el-option>
						</el-select>
					</el-form-item>
					<el-form-item label="下单人用户名">
						<el-input class="order-center-jl-page__filter-input order-center-jl-page__filter-input--wide"
							@keydown.enter.native="select" placeholder="请输入下单人用户名" v-model="userName"></el-input>
					</el-form-item>
					<el-form-item label="联系电话">
						<el-input class="order-center-jl-page__filter-input order-center-jl-page__filter-input--wide"
							@keydown.enter.native="select" placeholder="请输入联系电话" v-model="phone"></el-input>
					</el-form-item>
					<el-form-item label="订单编号">
						<el-input class="order-center-jl-page__filter-input order-center-jl-page__filter-input--wide"
							@keydown.enter.native="select" placeholder="请输入订单编号" v-model="ordersNo"></el-input>
					</el-form-item>
					<el-form-item label="教练用户名">
						<el-input class="order-center-jl-page__filter-input order-center-jl-page__filter-input--wide"
							@keydown.enter.native="select" placeholder="请输入教练用户名" v-model="artificerName"></el-input>
					</el-form-item>
					<el-form-item label="推广教练用户名">
						<el-input class="order-center-jl-page__filter-input order-center-jl-page__filter-input--wide"
							@keydown.enter.native="select" placeholder="请输入推广教练用户名" v-model="oneArtificerUserName"></el-input>
					</el-form-item>
					<el-form-item label="推广员用户名">
						<el-input class="order-center-jl-page__filter-input order-center-jl-page__filter-input--wide"
							@keydown.enter.native="select" placeholder="请输入推广员用户名" v-model="oneUserName"></el-input>
					</el-form-item>
					<el-form-item label="商家名称">
						<el-input class="order-center-jl-page__filter-input order-center-jl-page__filter-input--wide"
							@keydown.enter.native="select" placeholder="请输入商家名称" v-model="shopName"></el-input>
					</el-form-item>
					<el-form-item label="服务开始时间">
						<el-date-picker class="order-center-jl-page__filter-date" v-model="startTimeF" align="right"
							type="datetime" format="yyyy-MM-dd" value-format="yyyy-MM-dd" placeholder="选择开始时间">
						</el-date-picker>
					</el-form-item>
					<el-form-item label="服务截止时间">
						<el-date-picker class="order-center-jl-page__filter-date" v-model="endTimeF" align="right"
							type="datetime" format="yyyy-MM-dd" value-format="yyyy-MM-dd" placeholder="选择截止时间">
						</el-date-picker>
					</el-form-item>
					<el-form-item label="下单开始时间">
						<el-date-picker class="order-center-jl-page__filter-date" v-model="startTime" align="right"
							type="datetime" format="yyyy-MM-dd" value-format="yyyy-MM-dd" placeholder="选择开始时间">
						</el-date-picker>
					</el-form-item>
					<el-form-item label="下单截止时间">
						<el-date-picker class="order-center-jl-page__filter-date" v-model="endTime" align="right"
							type="datetime" format="yyyy-MM-dd" value-format="yyyy-MM-dd" placeholder="选择截止时间">
						</el-date-picker>
					</el-form-item>
					<el-form-item class="order-center-jl-page__filter-actions">
						<el-button type="primary" icon="el-icon-search" @click="select">查询</el-button>
						<el-button type="primary" icon="el-icon-refresh-left" @click="cleans">重置</el-button>
						<el-button type="warning" icon="el-icon-download"
							:disabled="!isAuth('orderCenterJl:daochu')" @click="exportBtn">导出Excel</el-button>
					</el-form-item>
				</el-form>
				<div class="order-center-jl-page__notice order-center-jl-page__notice--warn">
					* 导出订单提示：导出订单前请进行时间或者状态等筛选，否则导出订单量过多易出现卡顿或系统崩溃
				</div>
			</div>

			<div class="nf-table-card">
			<el-table v-loading="tableDataLoading" :data="tableData.list">
				<el-table-column prop="ordersId" label="编号" width="80" fixed="left">
				</el-table-column>
				<el-table-column prop="userName" label="下单用户">
					<template slot-scope="scope">
						<span class="order-center-jl-page__link"
							@click="updates(scope.row.userId)">{{ scope.row.userName?scope.row.userName:'未绑定' }}</span>
					</template>
				</el-table-column>
				<el-table-column prop="userName" label="教练用户">
					<template slot-scope="scope">
						<span class="order-center-jl-page__link"
							@click="updates(scope.row.artificerUserId)">{{ scope.row.artificerName?scope.row.artificerName:'未绑定' }}</span>
					</template>
				</el-table-column>

				<el-table-column prop="ordersType" label="订单类型">
					<template slot-scope="scope">
						<span v-if="scope.row.ordersType==1">上门订单</span>
						<span v-if="scope.row.ordersType==2">到店订单</span>
					</template>
				</el-table-column>
				<el-table-column prop="ordersNo" label="订单编号" width="100">
				</el-table-column>
				<el-table-column prop="shopName" label="商家名称">
				</el-table-column>
				<el-table-column prop="price" label="项目类型">
					<template slot-scope="scope">
						<span
							v-if="scope.row.ordersMassageList&&scope.row.ordersMassageList[0].massageType">{{ scope.row.ordersMassageList[0].massageType.classifyName }}</span>
					</template>
				</el-table-column>
				<el-table-column prop="price" label="项目名称">
					<template slot-scope="scope">
						<span
							v-if="scope.row.ordersMassageList&&scope.row.ordersMassageList[0].massageType">{{ scope.row.ordersMassageList[0].massageType.title }}</span>
					</template>
				</el-table-column>

				<el-table-column prop="price" label="项目价格">
				</el-table-column>
				<el-table-column prop="couponName" label="优惠券名称">
				</el-table-column>
				<el-table-column prop="couponMoney" label="优惠券价格">
				</el-table-column>
				<el-table-column prop="artificerMoney" label="教练收益">
				</el-table-column>
				<el-table-column prop="payMoney" label="支付金额">
				</el-table-column>
				<el-table-column prop="taxiMoney" label="打车费">
				</el-table-column>
				<el-table-column prop="oneUserName" label="推广用户名称">
					<template slot-scope="scope">
						<span class="order-center-jl-page__link"
							@click="updates(scope.row.oneUserId)">{{ scope.row.oneUserName?scope.row.oneUserName:'未绑定' }}</span>
					</template>
				</el-table-column>
				<el-table-column prop="oneUserMoney" label="推广员用户佣金">
				</el-table-column>
				<el-table-column prop="oneArtificerUserName" label="推广教练用户名">
					<template slot-scope="scope">
						<span style="color: #B94A48;cursor: pointer;"
							@click="updates(scope.row.oneArtificerUserId)">{{ scope.row.oneArtificerUserName?scope.row.oneArtificerUserName:'未绑定' }}</span>
					</template>
				</el-table-column>
				<el-table-column prop="oneArtificerMoney" label="推广教练佣金">
				</el-table-column>
				<el-table-column prop="shopMoney" label=" 商家收益">
					<template slot-scope="scope">
						<span style="color: #B94A48;">{{ scope.row.shopMoney?scope.row.shopMoney:'0' }}</span>
					</template>
				</el-table-column>
				<el-table-column prop="pingMoney" label="平台收益">
					<template slot-scope="scope">
						<span style="color: #B94A48;">{{ scope.row.pingMoney?scope.row.pingMoney:'0' }}</span>
					</template>
				</el-table-column>
				<!-- <el-table-column prop="cityAgencyMoney" label="城市代理收益">
					<template slot-scope="scope">
						<span style="color: #B94A48;" >{{ scope.row.cityAgencyMoney?scope.row.cityAgencyMoney:'0' }}</span>
					</template>
				</el-table-column>
				<el-table-column prop="sysUserName" label="代理商名称">
				</el-table-column> -->
				<el-table-column prop="addMoney" label="加钟价格">
				</el-table-column>
				<el-table-column prop="addTime" label="加钟时长">
				</el-table-column>
				<el-table-column prop="addNum" label="加钟次数">
				</el-table-column>
				<el-table-column prop="addArtificerMoney" label=" 加钟教练收益">
				</el-table-column>
				<el-table-column prop="sumArtificerMoney" label="教练总收益">
				</el-table-column>
				<el-table-column prop="sumMoney" label="订单总金额">
					<template slot-scope="scope">
						<span style="color: #B94A48;">{{ scope.row.sumMoney?scope.row.sumMoney:'0' }}</span>
					</template>
				</el-table-column>
				<el-table-column prop="km" label="全程距离(公里)">
				</el-table-column>
				<el-table-column prop="tripWay" label="出行方式">
					<template slot-scope="scope">
						<span v-if="scope.row.tripWay==1">公交</span>
						<span v-if="scope.row.tripWay==2">出租</span>
						<span v-if="scope.row.tripWay==3">免费</span>
					</template>
				</el-table-column>

				<el-table-column prop="payWay" label="支付方式">
					<template slot-scope="scope">
						<span v-if="scope.row.payWay==1">app微信</span>
						<span v-if="scope.row.payWay==2">微信公众号</span>
						<span v-if="scope.row.payWay==3">微信小程序</span>
						<span v-if="scope.row.payWay==4">微信公众号浏览器支付</span>
						<span v-if="scope.row.payWay==5">零钱</span>
						<span v-if="scope.row.payWay==6">支付宝</span>
					</template>
				</el-table-column>
				<el-table-column prop="phone" label="联系电话">
				</el-table-column>

				<el-table-column prop="address" label="服务地址" width="200">
					<template slot-scope="scope">
						<span>{{scope.row.address}}</span>
					</template>
				</el-table-column>
				<el-table-column prop="serveTime" label="预约时间" width="150">
					<template slot-scope="scope">
						<span>{{scope.row.serveTime}}</span>
						<el-button size="mini" type="primary" plain @click="updateBz(scope.row)"
							style="margin:5px;">修改</el-button>
					</template>
				</el-table-column>
				<el-table-column prop="remark" label="备注" width="200">
					<template slot-scope="scope">
						<span style="color: #f56c6c;">{{scope.row.remark}}</span>
						<el-button size="mini" type="primary" plain @click="updateBz(scope.row)"
							style="margin:5px;">修改</el-button>
					</template>
				</el-table-column>
				<el-table-column prop="startTime" label="开始服务时间" width="100">
				</el-table-column>
				<el-table-column prop="endTime" label="预估结束时间" width="100">
				</el-table-column>
				<!-- 新增的 待对接 -->
				<el-table-column prop="startAddress" label="开始服务地址" width="200">
				</el-table-column>
				<el-table-column prop="accomplishAddress" label="完成服务地址" width="200">
				</el-table-column>
				<el-table-column prop="startImg" label="服务开始图片" width="180">
					<template slot-scope="scope">
						<div style="display:flex;flex-wrap: wrap;" v-if="scope.row.startImg">
							<div v-for="(item,index) in scope.row.startImg.split(',')" :key="index">
								<el-popover placement="top-start" title="" trigger="hover">
									<img style="width: 50px; height: 50px;margin: 3px;" :src="item" alt=""
										slot="reference">
									<img style="width: 300px;height:auto" :src="item" alt="">
								</el-popover>
							</div>
						</div>
					</template>
				</el-table-column>
				<!-- <el-table-column prop="artificerImg" label="服务结束后图片" width="180">
					<template slot-scope="scope">
						<div style="display:flex;flex-wrap: wrap;" v-if="scope.row.artificerImg">
							<div v-for="(item,index) in scope.row.artificerImg.split(',')" :key="index">
								<el-popover placement="top-start" title="" trigger="hover">
									<img style="width: 50px; height: 50px;margin: 3px;" :src="item" alt=""
										slot="reference">
									<img style="width: 300px;height:auto" :src="item" alt="">
								</el-popover>
							</div>
						</div>
					</template>
				</el-table-column> -->
				<el-table-column prop="startLongitude" label="服务开始经度"></el-table-column>
				<el-table-column prop="startLatitude" label="服务开始纬度"></el-table-column>
				<el-table-column prop="accomplishLongitude" label="服务结束经度"></el-table-column>
				<el-table-column prop="accomplishLatitude" label="服务结束纬度"></el-table-column>
				<!--  -->
				<el-table-column prop="artificerStartTime" label="教练出发时间">
				</el-table-column>
				<el-table-column prop="artificerEndTime" label="教练到达时间">
				</el-table-column>
				<el-table-column prop="endTimes" label="订单完成时间" width="100">
				</el-table-column>

				<el-table-column prop="overTimeOrders" label="是否超时" fixed="right" width="50">
					<template slot-scope="scope">
						<span style="color: #f56c6c;" v-if="scope.row.overTimeOrders==1">是</span>
						<span style="color: #999;" v-else>否</span>
					</template>
				</el-table-column>
				<el-table-column prop="status" label="是否加钟" fixed="right" width="50">
					<template slot-scope="scope">
						<span style="color: #f56c6c;" v-if="scope.row.addNum>0">是</span>
						<span style="color: #999;" v-else>否</span>
					</template>
				</el-table-column>
				<el-table-column prop="status" label="订单状态" fixed="right" width="50">
					<template slot-scope="scope">
						<span style="color: #f56c6c;" v-if="scope.row.status==1">待支付</span>
						<span style="color: #E87D0D;" v-if="scope.row.status==2">待服务</span>
						<span style="color: #009900;" v-if="scope.row.status==3">待评价</span>
						<span style="color: #999;" v-if="scope.row.status==4">已取消</span>
						<span style="color: #999;" v-if="scope.row.status==5">已完成</span>
						<span style="color: red;" v-if="scope.row.status==6">服务中</span>
						<span style="color: #E87D0D;" v-if="scope.row.status==7">教练出发</span>
						<span style="color: #E87D0D;" v-if="scope.row.status==8">教练到达</span>
					</template>
				</el-table-column>
				<el-table-column prop="shopPhone" label="商家电话">
				</el-table-column>
				<el-table-column prop="shopAddress" label="商家地址" width="150">
				</el-table-column>
				<el-table-column prop="createTime" label="创建时间" width="160">
				</el-table-column>
				<el-table-column prop="createTime" fixed="right" label="操作" width="180">
					<template slot-scope="scope">
						<el-button style="margin:5px;" size="mini" type="primary" icon="document"
							@click="selectBtn(scope.row)">加钟记录
						</el-button>
						<el-button style="margin:5px;" size="mini" type="primary" icon="document"
							@click="pingjiaBtn(scope.row)" plain>评价
						</el-button>
						<el-button size="mini" type="danger" :disabled="!isAuth('orderCenterJl:wancheng')"
							v-if="scope.row.status==2||scope.row.status==6||scope.row.status==7||scope.row.status==8"
							@click="wancheng(scope.row)" style="margin:5px;" plain>完成订单</el-button>
						<el-button style="margin:5px;" size="mini" type="warning" icon="document" plain
							:disabled="!isAuth('orderCenterJl:zuandan')" @click="zhuandanBtn(scope.row)"
							v-if="scope.row.status!=1&&scope.row.status!=4&&scope.row.status!=3&&scope.row.status!=5">转单
						</el-button>
						<el-button style="margin:5px;" size="mini" type="warning" icon="document"
							:disabled="!isAuth('orderCenterJl:quxiao')" v-if="scope.row.status==7||scope.row.status==8"
							@click="quxiaoBtn(scope.row)" plain>取消订单
						</el-button>
						<el-button style="margin:5px;" size="mini" type="warning" icon="document"
							:disabled="!isAuth('orderCenterJl:tuikuan')" @click="tuikuanBtn(scope.row)"
							v-if="scope.row.status!=1&&scope.row.status!=4&&scope.row.status!=3&&scope.row.status!=5">退全款
						</el-button>
						<el-button size="mini" type="danger" :disabled="!isAuth('orderCenterJl:delete')"
							@click="deletes(scope.row)" style="margin:5px;">删除</el-button>
						<el-button style="margin:5px;" size="mini" type="warning" icon="document"
							:disabled="!isAuth('orderCenterJl:tuikuan')" @click="tuikuanBtnBf(scope.row)"
							v-if="scope.row.status!=1&&scope.row.status!=4&&scope.row.status!=3&&scope.row.status!=5">部分退款
						</el-button>
					</template>
				</el-table-column>
			</el-table>
			<div class="order-center-jl-page__summary">
				本页累计总交易统计：<span class="order-center-jl-page__summary-num">{{totalMoney.toFixed(2)}}</span>元；
				教练统计：<span class="order-center-jl-page__summary-num">{{totalMoneyS.toFixed(2)}}</span>元；
				推广用户统计：<span class="order-center-jl-page__summary-num">{{totalMoneyTy.toFixed(2)}}</span>元；
				推广教练统计：<span class="order-center-jl-page__summary-num">{{totalMoneyTj.toFixed(2)}}</span>元；
				商家统计：<span class="order-center-jl-page__summary-num">{{totalMoneyShop.toFixed(2)}}</span>元；
				平台统计：<span class="order-center-jl-page__summary-num">{{totalMoneyP.toFixed(2)}}</span>元
			</div>
			<div class="order-center-jl-page__pagination">
				<el-pagination @size-change="handleSizeChange" @current-change="handleCurrentChange"
					:page-sizes="[10, 20, 30, 40]" :page-size="limit" :current-page="page"
					layout="total,sizes, prev, pager, next,jumper" :total="tableData.totalCount">
				</el-pagination>
			</div>
			</div>
		</el-tab-pane>

		<!-- 加钟记录 -->
		<el-dialog title="加钟记录" :visible.sync="dialogFormVisibleJz" center width="80%">
			<el-table v-loading="tableDataLoading1" :data="jiazhongData">
				<el-table-column prop="ordersId" label="编号" width="80">
				</el-table-column>
				<el-table-column prop="ordersMassageList" label="加钟名称">
					<template slot-scope="scope">
						<span style="color: #f56c6c;"
							v-if="scope.row.ordersMassageList.length>0">{{scope.row.ordersMassageList[0].massageType?scope.row.ordersMassageList[0].massageType.title:''}}</span>
					</template>
				</el-table-column>
				<el-table-column prop="payMoney" label="加钟金额">
				</el-table-column>
				<el-table-column prop="artificerMoney" label="教练收益">
				</el-table-column>
				<el-table-column prop="oneArtificerMoney" label="推广教练佣金">
				</el-table-column>
				<el-table-column prop="shopMoney" label=" 商家收益">
					<template slot-scope="scope">
						<span style="color: #B94A48;">{{ scope.row.shopMoney?scope.row.shopMoney:'0' }}</span>
					</template>
				</el-table-column>
				<el-table-column prop="pingMoney" label="平台收益">
					<template slot-scope="scope">
						<span style="color: #B94A48;">{{ scope.row.pingMoney?scope.row.pingMoney:'0' }}</span>
					</template>
				</el-table-column>
				<!-- <el-table-column prop="cityAgencyMoney" label="城市代理收益">
					<template slot-scope="scope">
						<span style="color: #B94A48;" >{{ scope.row.cityAgencyMoney?scope.row.cityAgencyMoney:'0' }}</span>
					</template>
				</el-table-column> -->
				<el-table-column prop="duration" label="加钟时长">
					<template slot-scope="scope">
						<span
							v-if="scope.row.ordersMassageList.length>0">{{scope.row.ordersMassageList[0].massageType?scope.row.ordersMassageList[0].massageType.duration:''}}</span>
					</template>
				</el-table-column>
				<el-table-column prop="createTime" label="创建时间">
				</el-table-column>
			</el-table>
		</el-dialog>
		<!-- 评价记录 -->
		<el-dialog title="评价记录" :visible.sync="dialogFormVisiblePj" center>
			<el-table v-loading="tableDataLoading1" :data="jiazhongData">
				<el-table-column prop="id" label="编号" width="80">
				</el-table-column>
				<el-table-column prop="content" label="评价内容">
				</el-table-column>
				<el-table-column prop="score" label="评分" width="100">
				</el-table-column>
				<el-table-column prop="createTime" label="评价时间" width="160">
				</el-table-column>
			</el-table>
		</el-dialog>
		<!-- 修改备注时间 -->
		<el-dialog title="修改备注时间" :visible.sync="dialogFormVisibleBz" center>
			<div style="margin-bottom: 10px;">
				<span style="width: 200px;display: inline-block;text-align: right;">预约时间：</span>
				<el-date-picker style="width: 50%;" v-model="serveTime" align="right" type="datetime"
					format="yyyy-MM-dd HH:mm" value-format="yyyy-MM-dd HH:mm" placeholder="选择开始时间">
				</el-date-picker>&nbsp;&nbsp;&nbsp;
			</div>
			<div style="margin-bottom: 10px;">
				<span style="width: 200px;display: inline-block;text-align: right;">时间：</span>
				<!-- <el-checkbox-group v-model="times">
					<el-checkbox v-for="(item,index) in timeLis" :key="index" :label="item.times"
						:disabled="item.classify==1">{{item.times}}</el-checkbox>
				</el-checkbox-group> -->
				<el-radio-group v-model="times" style="margin-left: 200px;">
					<el-radio v-for="(item,index) in timeLis" :key="index" :label="item.times"
						style="margin: 5px;">{{item.times}}</el-radio>
				</el-radio-group>
			</div>
			<div style="margin-bottom: 10px;">
				<span
					style="width: 200px;display: inline-block;text-align: right;position: relative;top: -70px;">备注：</span>
				<el-input style="width:50%;" v-model="content" type="textarea" :rows="4" placeholder="请输入备注">
				</el-input>
			</div>
			<div slot="footer" class="dialog-footer">
				<el-button @click="dialogFormVisibleBz = false">取 消</el-button>
				<el-button type="primary" @click="replyNoticeTo()">确 定</el-button>
			</div>
		</el-dialog>
		<!-- 修改备注时间 -->
		<el-dialog title="部分退款" :visible.sync="dialogFormVisibleBftk" center>
			<div style="margin-bottom: 10px;">
				<span style="width: 200px;display: inline-block;text-align: right;">订单总金额：</span>
				<span style="color: brown;"> ¥ {{zongjine}}</span>( 打车费:<span style="color: brown;"> ¥
					{{dacefei}}</span>;项目金额:<span style="color: brown;"> ¥ {{xiangmujin}}</span>)
			</div>
			<div style="margin-bottom: 10px;">
				<span style="width: 200px;display: inline-block;text-align: right;">退款金额：</span>
				<el-input style="width:50%;" v-model="content" type="number" placeholder="请输入备注">
				</el-input>
			</div>
			<div slot="footer" class="dialog-footer">
				<el-button @click="dialogFormVisibleBftk = false">取 消</el-button>
				<el-button type="primary" @click="replyNoticeTk()">确 定</el-button>
			</div>
		</el-dialog>
		<!-- 转单弹框 -->
		<el-dialog title="转单" :visible.sync="dialogFormVisibleShifu" center>
			<div style="margin-bottom: 10px;">
				<span style="width: 200px;display: inline-block;text-align: right;">教练手机号：</span>
				<el-input style="width:50%;" v-model="content" type="number" placeholder="请输入教练手机号">
				</el-input>
			</div>
			<div slot="footer" class="dialog-footer">
				<el-button @click="dialogFormVisibleShifu = false">取 消</el-button>
				<el-button type="primary" @click="zdNoticeTk()">确 定</el-button>
			</div>
		</el-dialog>
	</el-tabs>
	</div>
</template>

<script>
	export default {
		data() {
			return {
				limit: 10,
				page: 1,
				state: 2,
				status: '',
				userName: '',
				type: '',
				id: '',
				statesnum: [{
						value: '',
						label: '全部'
					},
					{
						value: 1,
						label: '待支付'
					},
					{
						value: 2,
						label: '待服务'
					},
					{
						value: 3,
						label: '待评价'
					},
					{
						value: 4,
						label: '已取消'
					},
					{
						value: 5,
						label: '已完成'
					},
					{
						value: 6,
						label: '服务中'
					},
					{
						label: '教练出发',
						value: 7
					},
					{
						label: '教练到达',
						value: 8
					},
				],
				ordersType: '',
				typeNum: [{
						value: '',
						label: '全部'
					},
					{
						value: 1,
						label: '上门订单'
					},
					{
						value: 2,
						label: '到店订单'
					},
				],
				form: {
					id: '',
					title: '',
					url: '',
					type: '',
					state: '',
					types: [{
							value: 'word',
							label: '文字'
						},
						{
							value: 'url',
							label: '链接'
						}
					],
				},
				phone: '',

				flag: '',
				flags: [{
						value: 1,
						label: '单个用户'
					},
					{
						value: 2,
						label: '全部用户'
					}
				],
				formLabelWidth: '200px',
				activeName: 'first',
				tableDataLoading: false,
				dialogFormVisible1: false,
				dialogFormVisible2: false,
				dialogFormVisible: false,
				tableData: [],
				city: '',
				dialogFormVisibleJz: false,
				jiazhongData: [],
				dialogFormVisiblePj: false,
				tableDataLoading1: false,
				totalMoney: 0,
				totalMoneyS: 0,
				totalMoneyTj: 0,
				totalMoneyTy: 0,
				totalMoneyP: 0,
				totalMoneyShop: 0,
				statusJz: 0,
				overTimeOrders: '',
				statesnumJz: [{
						value: 0,
						label: '全部'
					},
					{
						value: 1,
						label: '是'
					},
					{
						value: 2,
						label: '否'
					}
				],
				statesnumCs: [{
						value: '',
						label: '全部'
					},
					{
						value: 1,
						label: '是'
					},
					{
						value: 2,
						label: '否'
					}
				],
				info: {
					stockDate: this.getNowTime(), //日期
				},
				info2: {
					stockDate2: this.getNowTime2(), //日期
				},
				startTime: '',
				endTime: '',
				startTimeF: '',
				endTimeF: '',
				artificerName: '',
				oneArtificerUserName: '',
				oneUserName: '',
				ordersNo: '',

				dialogFormVisibleBz: false,
				ordersId: '',
				content: '',
				serveTime: '',
				shopName: '',
				sysUserName: '',

				zongjine: 0,
				dacefei: 0,
				xiangmujin: 0,
				dialogFormVisibleBftk: false,

				fwData: [],
				classifyId: '',
				title: '',
				timeLis: [{
						times: '00:00',
						classify: 2
					}, {
						times: '01:00',
						classify: 2
					}, {
						times: '02:00',
						classify: 2
					}, {
						times: '03:00',
						classify: 2
					}, {
						times: '04:00',
						classify: 2
					},
					{
						times: '05:00',
						classify: 2
					}, {
						times: '06:00',
						classify: 2
					}, {
						times: '07:00',
						classify: 2
					}, {
						times: '08:00',
						classify: 2
					},
					{
						times: '09:00',
						classify: 2
					}, {
						times: '10:00',
						classify: 2
					}, {
						times: '11:00',
						classify: 2
					}, {
						times: '12:00',
						classify: 2
					},
					{
						times: '13:00',
						classify: 2
					}, {
						times: '14:00',
						classify: 2
					}, {
						times: '15:00',
						classify: 2
					}, {
						times: '16:00',
						classify: 2
					},
					{
						times: '17:00',
						classify: 2
					}, {
						times: '18:00',
						classify: 2
					}, {
						times: '19:00',
						classify: 2
					}, {
						times: '20:00',
						classify: 2
					},
					{
						times: '21:00',
						classify: 2
					}, {
						times: '22:00',
						classify: 2
					}, {
						times: '23:00',
						classify: 2
					},
				],
				timeLis2: [{
					times: '00:00',
					classify: 2
				}, {
					times: '00:30',
					classify: 2
				}, {
					times: '01:00',
					classify: 2
				}, {
					times: '01:30',
					classify: 2
				}, {
					times: '02:00',
					classify: 2
				}, {
					times: '02:30',
					classify: 2
				}, {
					times: '03:00',
					classify: 2
				}, {
					times: '03:30',
					classify: 2
				}, {
					times: '04:00',
					classify: 2
				}, {
					times: '04:30',
					classify: 2
				}, {
					times: '05:00',
					classify: 2
				}, {
					times: '05:30',
					classify: 2
				}, {
					times: '06:00',
					classify: 2
				}, {
					times: '06:30',
					classify: 2
				}, {
					times: '07:00',
					classify: 2
				}, {
					times: '07:30',
					classify: 2
				}, {
					times: '08:00',
					classify: 2
				}, {
					times: '08:30',
					classify: 2
				}, {
					times: '09:00',
					classify: 2
				}, {
					times: '09:30',
					classify: 2
				}, {
					times: '10:00',
					classify: 2
				}, {
					times: '10:30',
					classify: 2
				}, {
					times: '11:00',
					classify: 2
				}, {
					times: '11:30',
					classify: 2
				}, {
					times: '12:00',
					classify: 2
				}, {
					times: '12:30',
					classify: 2
				}, {
					times: '13:00',
					classify: 2
				}, {
					times: '13:30',
					classify: 2
				}, {
					times: '14:00',
					classify: 2
				}, {
					times: '14:30',
					classify: 2
				}, {
					times: '15:00',
					classify: 2
				}, {
					times: '15:30',
					classify: 2
				}, {
					times: '16:00',
					classify: 2
				}, {
					times: '16:30',
					classify: 2
				}, {
					times: '17:00',
					classify: 2
				}, {
					times: '17:30',
					classify: 2
				}, {
					times: '18:00',
					classify: 2
				}, {
					times: '18:30',
					classify: 2
				}, {
					times: '19:00',
					classify: 2
				}, {
					times: '19:30',
					classify: 2
				}, {
					times: '20:00',
					classify: 2
				}, {
					times: '20:30',
					classify: 2
				}, {
					times: '21:00',
					classify: 2
				}, {
					times: '21:30',
					classify: 2
				}, {
					times: '22:00',
					classify: 2
				}, {
					times: '22:30',
					classify: 2
				}, {
					times: '23:00',
					classify: 2
				}, {
					times: '23:30',
					classify: 2
				}, ],
				times: '',
				bili: 0,
				dialogFormVisibleShifu: false,
			}
		},
		methods: {
			//处理默认选中当前日期
			getNowTime() {
				var now = new Date()
				var year = now.getFullYear() //得到年份
				var month = now.getMonth() //得到月份
				var date = now.getDate() //得到日期
				month = month + 1
				month = month.toString().padStart(2, '0')
				date = date.toString().padStart(2, '0')
				var defaultDate = `${year}-${month}-${date}`
				return defaultDate
				this.$set(this.info, 'stockDate', defaultDate)
			},
			//处理默认选中当前日期
			getNowTime2() {
				var now = new Date()
				var year = now.getFullYear() //得到年份
				var month = now.getMonth() - now.getMonth() //得到月份
				var date = now.getDate() - now.getDate() + 1 //得到日期
				month = month + 1
				month = month.toString().padStart(2, '0')
				date = date.toString().padStart(2, '0')
				var defaultDate = `${year}-${month}-${date}`
				return defaultDate
				this.$set(this.info, 'stockDate', defaultDate)
			},
			// 详情跳转
			updataDetails(row) {
				this.$router.push({
					path: '/userDetail',
					query: {
						userId: row.userId
					}
				});
			},
			handleSizeChange(val) {
				this.limit = val;
				this.dataSelect()
			},
			handleCurrentChange(val) {
				this.page = val;
				this.dataSelect()
			},
			handleClick(tab, event) {
				if (tab._props.label == '订单列表') {
					this.page = 1
					this.limit = 10
					this.state = 2
					this.dataSelect()
				}

			},
			// 消息推送
			magNotice() {
				this.dialogFormVisible2 = true
			},

			// 获取数据列表
			dataSelect() {
				var endTime = this.endTime
				if (this.endTime != '') {
					endTime = this.endTime + " 23:59:59"
				}
				this.tableDataLoading = true
				this.totalMoney = 0
				this.totalMoneyS = 0
				this.totalMoneyTy = 0
				this.totalMoneyTj = 0
				this.totalMoneyP = 0
				this.totalMoneyShop = 0
				this.$http({
					url: this.$http.adornUrl('artificer/selectOrdersList'),
					method: 'get',
					params: this.$http.adornParams({
						'page': this.page,
						'limit': this.limit,
						'phone': this.phone,
						'name': this.userName,
						'status': this.status,
						'city': this.city,
						'startTime': this.startTime,
						'endTime': endTime,
						'isAdd': this.statusJz,
						'artificerName': this.artificerName,
						'oneUserName': this.oneUserName,
						'oneArtificerUserName': this.oneArtificerUserName,
						'ordersNo': this.ordersNo,
						'startServeTime': this.startTimeF,
						'endServeTime': this.endTimeF,
						'overTimeOrders': this.overTimeOrders,
						'shopName': this.shopName,
						'classifyId': this.classifyId,
						'title': this.title,
						'sysUserName': this.sysUserName,
						'ordersType': this.ordersType
					})
				}).then(({
					data
				}) => {
					this.tableDataLoading = false
					for (var i in data.data.list) {
						if (data.data.list[i].sumMoney) {
							this.totalMoney = this.totalMoney + data.data.list[i].sumMoney
						}
						if (data.data.list[i].sumArtificerMoney) {
							this.totalMoneyS = this.totalMoneyS + data.data.list[i].sumArtificerMoney
						}
						if (data.data.list[i].oneUserMoney) {
							this.totalMoneyTy = this.totalMoneyTy + data.data.list[i].oneUserMoney
						}
						if (data.data.list[i].oneArtificerMoney) {
							this.totalMoneyTj = this.totalMoneyTj + data.data.list[i].oneArtificerMoney
						}
						if (data.data.list[i].pingMoney) {
							this.totalMoneyP = this.totalMoneyP + data.data.list[i].pingMoney
						}
						if (data.data.list[i].shopMoney) {
							this.totalMoneyShop = this.totalMoneyShop + data.data.list[i].shopMoney
						}

					}
					let returnData = data.data;
					this.tableData = returnData

				})
			},
			// 详情跳转
			updates(userId) {
				if (userId) {
					this.$router.push({
						path: '/userDetail',
						query: {
							userId: userId
						}
					})
				}

			},
			// 查询
			select() {
				this.page = 1
				this.limit = 10
				this.dataSelect()
			},
			// 重置
			cleans() {
				this.userName = ''
				this.status = ''
				this.type = ''
				this.phone = ''
				this.city = ''
				this.startTime = ''
				this.endTime = ''
				this.statusJz = 0
				this.overTimeOrders = ''
				this.artificerName = ''
				this.oneArtificerUserName = ''
				this.oneUserName = ''
				this.ordersNo = ''
				this.startTimeF = ''
				this.endTimeF = ''
				this.shopName = ''
				this.sysUserName = ''
				this.classifyId = ''
				this.title = ''
				this.ordersType = ''
				this.dataSelect()
			},
			// select选择事件
			animeDat(state) {
				this.page = 1
				this.dataSelect()
			},
			// 删除订单
			deletes(row) {
				let delid = row.ordersId
				this.$confirm(`确定删除此条信息?`, '提示', {
					confirmButtonText: '确定',
					cancelButtonText: '取消',
					type: 'warning'
				}).then(() => {
					this.$http({
						url: this.$http.adornUrl('artificer/deleteOrders?ordersId=' + delid),
						method: 'post',
						params: this.$http.adornParams({})
					}).then(({
						data
					}) => {
						if (data.code == 0) {
							this.$message({
								message: '删除成功',
								type: 'success',
								duration: 1500,
								onClose: () => {
									this.dataSelect()
								}
							})
						} else {
							this.$message({
								message: data.msg,
								type: 'warning',
								duration: 1500,
								onClose: () => {}
							})
						}
					})
				})
			},
			// 加钟记录弹框
			selectBtn(row) {
				this.dialogFormVisibleJz = true
				this.tableDataLoading1 = true
				this.$http({
					url: this.$http.adornUrl('artificer/selectOrdersList'),
					method: 'get',
					params: this.$http.adornParams({
						'page': 1,
						'limit': 10,
						'phone': '',
						'name': '',
						'status': 2,
						'city': '',
						'parentId': row.ordersId
					})
				}).then(({
					data
				}) => {
					this.tableDataLoading1 = false

					let returnData = data.data.list;
					this.jiazhongData = returnData

				})
			},
			// 订单评价
			pingjiaBtn(row) {
				this.dialogFormVisiblePj = true
				this.tableDataLoading1 = true
				this.jiazhongData = []
				this.$http({
					url: this.$http.adornUrl('takingComment/selectOrderTakingCommentByOrdersId'),
					method: 'get',
					params: this.$http.adornParams({
						'ordersId': row.ordersId
					})
				}).then(({
					data
				}) => {
					this.tableDataLoading1 = false
					if (data.data) {
						let returnData = data.data;
						this.jiazhongData.push(returnData)
					}
					console.log(this.tableDataLoading1)

				})
			},
			// 退款
			tuikuanBtn(row) {
				let delid = row.ordersId
				this.$confirm(`确定退款吗?`, '提示', {
					confirmButtonText: '确定',
					cancelButtonText: '取消',
					type: 'warning'
				}).then(() => {
					this.$http({
						url: this.$http.adornUrl('artificer/refundOrders?ordersId=' + delid),
						method: 'post',
						params: this.$http.adornParams({})
					}).then(({
						data
					}) => {
						if (data.code == 0) {
							this.$message({
								message: '操作成功',
								type: 'success',
								duration: 1500,
								onClose: () => {
									this.dataSelect()
								}
							})
						} else {
							this.$message({
								message: data.msg,
								type: 'warning',
								duration: 1500,
								onClose: () => {}
							})
						}

					})
				})
			},
			// 导出
			exportBtn() {
				var endTime = this.endTime
				if (this.endTime != '') {
					endTime = this.endTime + " 23:59:59"
				}
				this.$http({
					url: this.$http.adornUrl('artificer/excelOrdersList'),
					method: 'get',
					responseType: 'blob',
					params: this.$http.adornParams({
						// 'page': page,
						// 'size': this.size,
						'phone': this.phone,
						'name': this.userName,
						'status': this.status,
						'city': this.city,
						'startTime': this.startTime,
						'endTime': endTime,
						'isAdd': this.statusJz,
						'artificerName': this.artificerName,
						'oneUserName': this.oneUserName,
						'oneArtificerUserName': this.oneArtificerUserName,
						'ordersNo': this.ordersNo,
						'startServeTime': this.startTimeF,
						'endServeTime': this.endTimeF,
						'overTimeOrders': this.overTimeOrders,
						'shopName': this.shopName,
						'classifyId': this.classifyId,
						'title': this.title,
						'sysUserName': this.sysUserName
					})
				}).then(({
					data
				}) => {
					let blob = new Blob([data], {
						type: 'application/vnd.ms-excel,application/vnd.openxmlformats-officedocument.spreadsheetml.sheet'
					})
					if (window.navigator.msSaveOrOpenBlob) {
						navigator.msSaveBlob(blob)
					} else {
						let url = window.URL.createObjectURL(blob)
						let elink = document.createElement('a')
						elink.download = '订单列表.xlsx'
						elink.style.display = 'none'
						elink.href = url
						document.body.appendChild(elink)
						elink.click()
						document.body.removeChild(elink)
					}
				})
			},
			// 修改备注、时间
			updateBz(row) {
				var times1 = row.serveTime

				this.ordersId = row.ordersId
				this.serveTime = row.serveTime.slice(0, 11)
				this.times = row.serveTime.slice(11, 16)

				// for (var j in this.timeLis) {
				// 	if (this.times==this.timeLis[j].times) {
				// 		this.timeLis[j].classify = 1
				// 	}
				// }
				console.log(this.serveTime, 'row.times', this.times)
				this.content = row.remark
				this.dialogFormVisibleBz = true
			},
			// 修改
			replyNoticeTo() {
				var times1 = this.serveTime + '' + this.times
				this.$http({
					url: this.$http.adornUrl('artificer/updateOrders'),
					method: 'post',
					// params: this.$http.adornParams({
					data: this.$http.adornData({
						'ordersId': this.ordersId,
						'serveTime': times1,
						'remark': this.content,
					})
				}).then(({
					data
				}) => {
					if (data.code == 0) {
						this.$message({
							message: '修改成功',
							type: 'success',
							duration: 1500,
							onClose: () => {
								this.dataSelect()
							}
						})
						this.dialogFormVisibleBz = false
					} else {
						this.$message({
							message: data.msg,
							type: 'warning',
							duration: 1500,
							onClose: () => {}
						})
					}

				})
			},
			// 取消订单
			quxiaoBtn(row) {
				let delid = row.ordersId


				this.$confirm('当前教练已接单，若取消订单，系统将扣除' + this.bili + '%的手续费以及所需的车费！确定继续取消订单吗?', '提示', {
					confirmButtonText: '确定',
					cancelButtonText: '取消',
					type: 'warning'
				}).then(() => {
					this.$http({
						url: this.$http.adornUrl('artificer/cancelOrders?ordersId=' + delid),
						method: 'post',
						params: this.$http.adornParams({})
					}).then(({
						data
					}) => {
						if (data.code == 0) {
							this.$message({
								message: '操作成功',
								type: 'success',
								duration: 1500,
								onClose: () => {
									this.dataSelect()
								}
							})
						} else {
							this.$message({
								message: data.msg,
								type: 'warning',
								duration: 1500,
								onClose: () => {}
							})
						}

					})
				})
			},
			// 完成订单
			wancheng(row) {
				let delid = row.ordersId
				this.$confirm(`确定进行完成订单操作吗?`, '提示', {
					confirmButtonText: '确定',
					cancelButtonText: '取消',
					type: 'warning'
				}).then(() => {
					this.$http({
						url: this.$http.adornUrl('artificer/accomplishOrders?ordersId=' + delid),
						method: 'post',
						params: this.$http.adornParams({})
					}).then(({
						data
					}) => {
						if (data.code == 0) {
							this.$message({
								message: '操作成功',
								type: 'success',
								duration: 1500,
								onClose: () => {
									this.dataSelect()
								}
							})
						} else {
							this.$message({
								message: data.msg,
								type: 'warning',
								duration: 1500,
								onClose: () => {}
							})
						}

					})
				})
			},
			// 部分退款弹框
			tuikuanBtnBf(row) {
				this.content = ''
				this.ordersId = row.ordersId
				this.zongjine = Number(row.sumMoney)
				this.dacefei = row.taxiMoney
				this.xiangmujin = Number(row.sumMoney) - Number(row.taxiMoney)
				this.dialogFormVisibleBftk = true
			},
			// 确定退款
			replyNoticeTk(row) {
				if (this.content === '') {
					this.$notify({
						title: '提示',
						duration: 1800,
						message: '请输入退款金额',
						type: 'warning'
					});
					return
				} else if (this.content <= 0 || this.content >= this.zongjine) {
					this.$notify({
						title: '提示',
						duration: 1800,
						message: '请输入有效的金额',
						type: 'warning'
					});
					return
				}
				this.$confirm(`确定要退款吗?`, '提示', {
					confirmButtonText: '确定',
					cancelButtonText: '取消',
					type: 'warning'
				}).then(() => {
					this.$http({
						url: this.$http.adornUrl('artificer/refundOrdersMoney'),
						method: 'post',
						params: this.$http.adornParams({
							'ordersId': this.ordersId,
							'refundMoney': this.content
						})
					}).then(({
						data
					}) => {
						if (data.code == 0) {
							this.$message({
								message: '操作成功',
								type: 'success',
								duration: 1500,
								onClose: () => {
									this.dialogFormVisibleBftk = false
									this.dataSelect()
								}
							})
						} else {
							this.$message({
								message: data.msg,
								type: 'warning',
								duration: 1500,
								onClose: () => {}
							})
						}

					})
				})
			},
			// 项目类型
			fwSelect() {
				this.$http({
					url: this.$http.adornUrl('sys/dict/selectDictList'),
					method: 'get',
					params: this.$http.adornParams({
						'type': '服务类型'
					})
				}).then(({
					data
				}) => {
					let returnData = data.data
					this.fwData = returnData
				})
			},
			// 获取时间类型
			xianshi() {
				this.$http({
					url: this.$http.adornUrl('common/type/395'),
					method: 'get',
					data: this.$http.adornData({})
				}).then(({
					data
				}) => {
					if (data.code == 0) {
						if (data.data.value == '30') {
							// this.show = true
							this.timeLis = this.timeLis2
						}
					}
				})
				this.$http({
					url: this.$http.adornUrl('common/type/423'),
					method: 'get',
					data: this.$http.adornData({})
				}).then(({
					data
				}) => {
					if (data.code == 0) {
						this.bili = Number(data.data.value) * 100
					}
				})
			},
			// 转单弹框
			zhuandanBtn(row) {
				this.content = ''
				this.ordersId = row.ordersId
				this.dialogFormVisibleShifu = true

			},
			zdNoticeTk() {
				if (this.content == '') {
					this.$notify({
						title: '提示',
						duration: 1800,
						message: '请输入转单教练的手机号',
						type: 'warning'
					});
					return
				}
				this.$confirm(`确定要转单吗?`, '提示', {
					confirmButtonText: '确定',
					cancelButtonText: '取消',
					type: 'warning'
				}).then(() => {
					this.$http({
						url: this.$http.adornUrl('artificer/transferOrder'),
						method: 'post',
						params: this.$http.adornParams({
							'ordersId': this.ordersId,
							'phone': this.content
						})
					}).then(({
						data
					}) => {
						if (data.code == 0) {
							this.$message({
								message: '操作成功',
								type: 'success',
								duration: 1500,
								onClose: () => {
									this.dialogFormVisibleShifu = false
									this.dataSelect()
								}
							})
						} else {
							this.$message({
								message: data.msg,
								type: 'warning',
								duration: 1500,
								onClose: () => {}
							})
						}

					})
				})
			}
		},
		mounted() {
			this.dataSelect()
			// this.fwSelect()
			this.xianshi()
		}
	};
</script>

<style scoped>
.order-center-jl-page__filter-select {
	width: 150px;
}

.order-center-jl-page__filter-input {
	width: 150px;
}

.order-center-jl-page__filter-input--wide {
	width: 200px;
}

.order-center-jl-page__filter-date {
	width: 160px;
}

.order-center-jl-page__filter-actions .el-button + .el-button {
	margin-left: 12px;
}

.order-center-jl-page__tabs {
	margin-bottom: 0;
}

.order-center-jl-page__tabs >>> .el-tabs__header {
	margin-bottom: 0;
}

.order-center-jl-page__tabs >>> .el-tabs__item.is-active {
	color: #5cc477;
}

.order-center-jl-page__tabs >>> .el-tabs__active-bar {
	background-color: #5cc477;
}

.order-center-jl-page__tabs .nf-filter-card {
	margin-top: 12px;
}

.order-center-jl-page__tabs .nf-table-card {
	margin-top: 12px;
	margin-bottom: 0;
}

.order-center-jl-page__notice {
	margin-top: 4px;
	font-size: 13px;
	line-height: 1.5;
}

.order-center-jl-page__notice--warn {
	color: #e6a23c;
}

.order-center-jl-page__link {
	color: #5cc477;
	cursor: pointer;
}

.order-center-jl-page__summary {
	margin-top: 12px;
	font-size: 14px;
	color: #606266;
	line-height: 1.8;
}

.order-center-jl-page__summary-num {
	font-size: 16px;
	font-weight: 600;
	color: #5cc477;
}

.order-center-jl-page__pagination {
	display: flex;
	justify-content: center;
	padding-top: 12px;
}
</style>