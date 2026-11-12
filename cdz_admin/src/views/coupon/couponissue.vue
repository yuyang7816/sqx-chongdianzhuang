<template>
	<div class="nf-page couponissue-page">
		<div class="nf-page-hero">
			<span class="nf-page-hero__badge">已发布优惠券</span>
			<h2 class="nf-page-hero__title">已发布优惠券</h2>
			<p class="nf-page-hero__desc">管理已发布的积分兑换券与新人券，支持按充电站名称、优惠券名称筛选，可查询、重置、赠送、修改与删除。</p>
			<div class="nf-page-hero__bar"></div>
		</div>

		<div class="nf-filter-card">
			<div class="nf-filter-card__head">
				<i class="el-icon-search nf-filter-card__head-icon"></i>
				<span>筛选条件</span>
			</div>
			<el-form :inline="true" class="nf-filter-card__body" @submit.native.prevent>
				<el-form-item label="充电站名称">
					<el-input
						class="couponissue-page__filter-input"
						@keydown.enter.native="refresh"
						clearable
						placeholder="请输入充电站名称"
						v-model="store_nameT">
					</el-input>
				</el-form-item>
				<el-form-item label="优惠券名称">
					<el-input
						class="couponissue-page__filter-input"
						@keydown.enter.native="refresh"
						clearable
						placeholder="请输入优惠券名称"
						v-model="coupon_nameT">
					</el-input>
				</el-form-item>
				<el-form-item class="couponissue-page__filter-actions">
					<el-button type="primary" icon="el-icon-search" @click="refresh">查询</el-button>
					<el-button type="primary" icon="el-icon-refresh-left" @click="cleans">重置</el-button>
				</el-form-item>
			</el-form>
		</div>

		<el-tabs v-model="activeName" @tab-click="handleClick" class="couponissue-page__tabs">

			<!-- <el-tab-pane label="团购券" name="first">
				<el-table v-loading="tableDataLoading" :data="tableData.content">/
					<el-table-column prop="couponIssueId" label="编号" width="80" fixed="left">
					</el-table-column>
					<el-table-column prop="type" label="券类型" fixed="left">
						<template slot-scope="scope">
							<span v-if="scope.row.couponType == 1">优惠券</span>
							<span v-if="scope.row.couponType == 2">实物</span>
							<span v-if="scope.row.couponType == 3">定时券</span>
						</template>
					</el-table-column>
					<el-table-column prop="tearoom.tearoomName" label="充电站名称" width="160">
					</el-table-column>
					<el-table-column prop="goodsList.goodsName" label="充电桩名称" width="250">
						<template slot-scope="scope">
							<div v-if="scope.row.goodsList">
								<span v-for="(item,index) in scope.row.goodsList" :key="index">
									<span v-if="item!=null">{{item.goodsName}};</span>
								</span>
							</div>
						</template>
					</el-table-column>
					<el-table-column prop="couponName" label="优惠券名称" width="150" fixed="left">
					</el-table-column>
					<el-table-column prop="issueNumber" label="发布数量">
					</el-table-column>
					<el-table-column prop="remainNumber" label="剩余数量">
					</el-table-column>
					<el-table-column prop="hour" label="使用时长">
					</el-table-column>
					<el-table-column prop="minHour" label="最低使用时长(小时)">
					</el-table-column>
					<el-table-column prop="validDay" label="有效期限(天)">
					</el-table-column>
					<el-table-column prop="isLimitTime" label="是否限制时间">
						<template slot-scope="scope">
							<span v-if="scope.row.isLimitTime == 1">是</span>
							<span v-if="scope.row.isLimitTime == 2">否</span>
						</template>
					</el-table-column>
					<el-table-column prop="weeks" label="限制周期(可使用)" width="160">
						<template slot-scope="scope">
							<div v-if="scope.row.weeks">
								<span v-for="(item,index) in scope.row.weeks.split(',')" :key="index">
									<span v-if="item==1">周一；</span>
									<span v-if="item==2">周二；</span>
									<span v-if="item==3">周三；</span>
									<span v-if="item==4">周四；</span>
									<span v-if="item==5">周五；</span>
									<span v-if="item==6">周六；</span>
									<span v-if="item==7">周日；</span>
								</span>
							</div>
						</template>
					</el-table-column>
					<el-table-column prop="times" label="使用时间" width="160">
					</el-table-column>
					<el-table-column prop="teamRule" label="使用规则" width="250">
						<template slot-scope="scope">
							<el-popover placement="top-start" title="" trigger="hover">
								<div style="display: -webkit-box;-webkit-box-orient: vertical;-webkit-line-clamp: 3;overflow: hidden;max-height:80px;"
									slot="reference">
									{{scope.row.teamRule}}
								</div>
								<div style="width: 500px;height: auto;">{{scope.row.teamRule}}</div>
							</el-popover>
						</template>
					</el-table-column>
					<el-table-column prop="buyRule" label="购买须知" width="250">
						<template slot-scope="scope">
							<el-popover placement="top-start" title="" trigger="hover">
								<div style="display: -webkit-box;-webkit-box-orient: vertical;-webkit-line-clamp: 3;overflow: hidden;max-height:80px;"
									slot="reference">
									{{scope.row.buyRule}}
								</div>
								<div style="width: 500px;height: auto;">{{scope.row.buyRule}}</div>
							</el-popover>
						</template>
					</el-table-column>
					<el-table-column label="状态" fixed="right">
						<template slot-scope="scope">
							<el-switch v-model="scope.row.status" @change="change(scope.row)" :active-value="openValue"
								:inactive-value="closeValue" active-color="#13ce66" inactive-color="#ff4949">
							</el-switch>
						</template>
					</el-table-column>
					<el-table-column prop="createTime" label="创建时间" width="150">
					</el-table-column>
					<el-table-column label="操作" width="160" fixed="right">
						<template slot-scope="scope">
							<el-button style='margin:5px;' :disabled="!isAuth('coupon:add')" size="mini" type="primary"
								icon="document" @click="songCoupon(scope.row)">赠送优惠券</el-button>
							<el-button size="mini" type="primary" plain :disabled="!isAuth('coupon:update')"
								style='margin:5px;' @click="updateBtn(scope.row)">修改
							</el-button>
							<el-button size="mini" type="danger" :disabled="!isAuth('couponissue:delete')"
								style='margin:5px;' @click="shopdeletes(scope.row)">删除
							</el-button>
						</template>
					</el-table-column>
				</el-table>
				<div style="text-align: center;margin-top: 10px;">
					<el-pagination @size-change="handleSizeChange" @current-change="handleCurrentChange"
						:page-sizes="[10, 20, 30, 50, 100]" :page-size="size" :current-page="page"
						layout="total,sizes, prev, pager, next" :total="tableData.totalElements">
					</el-pagination>
				</div>
			</el-tab-pane> -->
			<el-tab-pane label="积分兑换券" name="second">
				<div class="nf-table-card">
				<el-table v-loading="tableDataLoading" :data="tableData.content">
					<el-table-column prop="couponIssueId" label="编号" width="80" fixed="left">
					</el-table-column>
					<!-- <el-table-column prop="type" label="券类型" fixed="left">
						<template slot-scope="scope">
							<span v-if="scope.row.couponType == 1">优惠券</span>
							<span v-if="scope.row.couponType == 2">实物</span>
							<span v-if="scope.row.couponType == 3">定时券</span>
						</template>
					</el-table-column> -->
					<el-table-column prop="tearoom.tearoomName" label="充电站名称" width="160" fixed="left">
					</el-table-column>
					<el-table-column prop="goodsList.goodsName" label="充电桩名称" width="250">
						<template slot-scope="scope">
							<div v-if="scope.row.goodsList">
								<span v-for="(item,index) in scope.row.goodsList" :key="index">
									<span v-if="item!=null">{{item.goodsName}};</span>
								</span>
							</div>
						</template>
					</el-table-column>
					<el-table-column prop="couponName" label="优惠券名称" width="150">
					</el-table-column>
					<el-table-column prop="issueNumber" label="发布数量">
					</el-table-column>
					<el-table-column prop="remainNumber" label="剩余数量">
					</el-table-column>
					<el-table-column prop="integral" label="兑换积分">
					</el-table-column>
					<!-- <el-table-column prop="hour" label="使用时长">
					</el-table-column>
					<el-table-column prop="minHour" label="最低使用时长(小时)">
					</el-table-column> -->
					<el-table-column prop="validDay" label="有效期限(天)">
					</el-table-column>
					<el-table-column prop="isLimitTime" label="是否限制时间">
						<template slot-scope="scope">
							<span v-if="scope.row.isLimitTime == 1">是</span>
							<span v-if="scope.row.isLimitTime == 2">否</span>
						</template>
					</el-table-column>
					<el-table-column prop="weeks" label="限制周期(可使用)" width="160">
						<template slot-scope="scope">
							<div v-if="scope.row.weeks">
								<span v-for="(item,index) in scope.row.weeks.split(',')" :key="index">
									<span v-if="item==1">周一；</span>
									<span v-if="item==2">周二；</span>
									<span v-if="item==3">周三；</span>
									<span v-if="item==4">周四；</span>
									<span v-if="item==5">周五；</span>
									<span v-if="item==6">周六；</span>
									<span v-if="item==7">周日；</span>
								</span>
							</div>
						</template>
					</el-table-column>
					<el-table-column prop="times" label="使用时间" width="160">
					</el-table-column>
					<el-table-column prop="teamRule" label="使用规则" width="250">
						<template slot-scope="scope">
							<el-popover placement="top-start" title="" trigger="hover">
								<div style="display: -webkit-box;-webkit-box-orient: vertical;-webkit-line-clamp: 3;overflow: hidden;max-height:80px;"
									slot="reference">
									{{scope.row.teamRule}}
								</div>
								<div style="width: 500px;height: auto;">{{scope.row.teamRule}}</div>
							</el-popover>
						</template>
					</el-table-column>
					<el-table-column prop="buyRule" label="购买须知" width="250">
						<template slot-scope="scope">
							<el-popover placement="top-start" title="" trigger="hover">
								<div style="display: -webkit-box;-webkit-box-orient: vertical;-webkit-line-clamp: 3;overflow: hidden;max-height:80px;"
									slot="reference">
									{{scope.row.buyRule}}
								</div>
								<div style="width: 500px;height: auto;">{{scope.row.buyRule}}</div>
							</el-popover>
						</template>
					</el-table-column>
					<el-table-column label="状态" fixed="right">
						<template slot-scope="scope">
							<el-switch v-model="scope.row.status" @change="change(scope.row)" :active-value="openValue"
								:inactive-value="closeValue" active-color="#5cc477" inactive-color="#ff4949">
							</el-switch>
						</template>
					</el-table-column>
					<el-table-column prop="createTime" label="创建时间" width="150">
					</el-table-column>
					<el-table-column label="操作" width="160" fixed="right">
						<template slot-scope="scope">
							<el-button style="margin: 3px;" :disabled="!isAuth('coupon:add')" size="mini" type="primary"
								@click="songCoupon(scope.row)">赠送优惠券</el-button>
							<el-button size="mini" type="primary" :disabled="!isAuth('coupon:update')"
								style="margin: 3px;" @click="updateBtn(scope.row)">修改
							</el-button>
							<el-button size="mini" type="danger" :disabled="!isAuth('couponissue:delete')"
								style="margin: 3px;" @click="shopdeletes(scope.row)">删除
							</el-button>
						</template>
					</el-table-column>
				</el-table>
				<div class="couponissue-page__pagination">
					<el-pagination @size-change="handleSizeChange" @current-change="handleCurrentChange"
						:page-sizes="[10, 20, 30, 50, 100]" :page-size="size" :current-page="page"
						layout="total,sizes, prev, pager, next" :total="tableData.totalElements">
					</el-pagination>
				</div>
				</div>
			</el-tab-pane>
			<el-tab-pane label="新人券" name="three">
				<div class="nf-table-card">
				<el-table v-loading="tableDataLoading" :data="tableData.content">
					<el-table-column prop="couponIssueId" label="编号" width="80" fixed="left">
					</el-table-column>
					<!-- <el-table-column prop="type" label="券类型" fixed="left">
						<template slot-scope="scope">
							<span v-if="scope.row.couponType == 1">优惠券</span>
							<span v-if="scope.row.couponType == 2">实物</span>
							<span v-if="scope.row.couponType == 3">定时券</span>
						</template>
					</el-table-column> -->
					<el-table-column prop="tearoom.tearoomName" label="充电站名称" width="160" fixed="left">
					</el-table-column>
					<el-table-column prop="goodsList.goodsName" label="充电桩名称" width="250">
						<template slot-scope="scope">
							<div v-if="scope.row.goodsList">
								<span v-for="(item,index) in scope.row.goodsList" :key="index">
									<span v-if="item!=null">{{item.goodsName}};</span>
								</span>
							</div>
						</template>
					</el-table-column>
					<el-table-column prop="couponName" label="优惠券名称" width="150">
					</el-table-column>
					<el-table-column prop="issueNumber" label="发布数量">
					</el-table-column>
					<el-table-column prop="remainNumber" label="剩余数量">
					</el-table-column>
					<!-- <el-table-column prop="integral" label="兑换积分">
					</el-table-column> -->
					<!-- <el-table-column prop="hour" label="使用时长">
					</el-table-column>
					<el-table-column prop="minHour" label="最低使用时长(小时)">
					</el-table-column> -->
					<el-table-column prop="validDay" label="有效期限(天)">
					</el-table-column>
					<el-table-column prop="isLimitTime" label="是否限制时间">
						<template slot-scope="scope">
							<span v-if="scope.row.isLimitTime == 1">是</span>
							<span v-if="scope.row.isLimitTime == 2">否</span>
						</template>
					</el-table-column>
					<el-table-column prop="weeks" label="限制周期(可使用)" width="160">
						<template slot-scope="scope">
							<div v-if="scope.row.weeks">
								<span v-for="(item,index) in scope.row.weeks.split(',')" :key="index">
									<span v-if="item==1">周一；</span>
									<span v-if="item==2">周二；</span>
									<span v-if="item==3">周三；</span>
									<span v-if="item==4">周四；</span>
									<span v-if="item==5">周五；</span>
									<span v-if="item==6">周六；</span>
									<span v-if="item==7">周日；</span>
								</span>
							</div>
						</template>
					</el-table-column>
					<el-table-column prop="times" label="使用时间" width="160">
					</el-table-column>
					<el-table-column prop="teamRule" label="使用规则" width="250">
						<template slot-scope="scope">
							<el-popover placement="top-start" title="" trigger="hover">
								<div style="display: -webkit-box;-webkit-box-orient: vertical;-webkit-line-clamp: 3;overflow: hidden;max-height:80px;"
									slot="reference">
									{{scope.row.teamRule}}
								</div>
								<div style="width: 500px;height: auto;">{{scope.row.teamRule}}</div>
							</el-popover>
						</template>
					</el-table-column>
					<el-table-column prop="buyRule" label="购买须知" width="250">
						<template slot-scope="scope">
							<el-popover placement="top-start" title="" trigger="hover">
								<div style="display: -webkit-box;-webkit-box-orient: vertical;-webkit-line-clamp: 3;overflow: hidden;max-height:80px;"
									slot="reference">
									{{scope.row.buyRule}}
								</div>
								<div style="width: 500px;height: auto;">{{scope.row.buyRule}}</div>
							</el-popover>
						</template>
					</el-table-column>
					<el-table-column label="状态" fixed="right">
						<template slot-scope="scope">
							<el-switch v-model="scope.row.status" @change="change(scope.row)" :active-value="openValue"
								:inactive-value="closeValue" active-color="#5cc477" inactive-color="#ff4949">
							</el-switch>
						</template>
					</el-table-column>
					<el-table-column prop="createTime" label="创建时间" width="150">
					</el-table-column>
					<el-table-column label="操作" width="160" fixed="right">
						<template slot-scope="scope">
							<el-button style="margin: 3px;" :disabled="!isAuth('coupon:add')" size="mini" type="primary"
								@click="songCoupon(scope.row)">赠送优惠券</el-button>
							<el-button size="mini" type="primary" :disabled="!isAuth('coupon:update')"
								style="margin: 3px;" @click="updateBtn(scope.row)">修改
							</el-button>
							<el-button size="mini" type="danger" :disabled="!isAuth('couponissue:delete')"
								style="margin: 3px;" @click="shopdeletes(scope.row)">删除
							</el-button>
						</template>
					</el-table-column>
				</el-table>
				<div class="couponissue-page__pagination">
					<el-pagination @size-change="handleSizeChange" @current-change="handleCurrentChange"
						:page-sizes="[10, 20, 30, 50, 100]" :page-size="size" :current-page="page"
						layout="total,sizes, prev, pager, next" :total="tableData.totalElements">
					</el-pagination>
				</div>
				</div>
			</el-tab-pane>
			<!-- <el-tab-pane label="会员券" name="four">
				<el-table v-loading="tableDataLoading" :data="tableData.content">
					<el-table-column prop="couponIssueId" label="编号" width="80" fixed="left">
					</el-table-column>
					<el-table-column prop="type" label="券类型" fixed="left">
						<template slot-scope="scope">
							<span v-if="scope.row.couponType == 1">优惠券</span>
							<span v-if="scope.row.couponType == 2">实物</span>
							<span v-if="scope.row.couponType == 3">定时券</span>
						</template>
					</el-table-column>
					<el-table-column prop="tearoom.tearoomName" label="充电站名称" width="160">
					</el-table-column>
					<el-table-column prop="goodsList.goodsName" label="充电桩名称" width="250">
						<template slot-scope="scope">
							<div v-if="scope.row.goodsList">
								<span v-for="(item,index) in scope.row.goodsList" :key="index">
									<span v-if="item!=null">{{item.goodsName}};</span>
								</span>
							</div>
						</template>
					</el-table-column>
					<el-table-column prop="couponName" label="优惠券名称" width="150" fixed="left">
					</el-table-column>
					<el-table-column prop="issueNumber" label="发布数量">
					</el-table-column>
					<el-table-column prop="remainNumber" label="剩余数量">
					</el-table-column>
					<el-table-column prop="hour" label="使用时长">
					</el-table-column>
					<el-table-column prop="minHour" label="最低使用时长(小时)">
					</el-table-column>
					<el-table-column prop="validDay" label="有效期限(天)">
					</el-table-column>
					<el-table-column prop="isLimitTime" label="是否限制时间">
						<template slot-scope="scope">
							<span v-if="scope.row.isLimitTime == 1">是</span>
							<span v-if="scope.row.isLimitTime == 2">否</span>
						</template>
					</el-table-column>
					<el-table-column prop="weeks" label="限制周期(可使用)" width="160">
						<template slot-scope="scope">
							<div v-if="scope.row.weeks">
								<span v-for="(item,index) in scope.row.weeks.split(',')" :key="index">
									<span v-if="item==1">周一；</span>
									<span v-if="item==2">周二；</span>
									<span v-if="item==3">周三；</span>
									<span v-if="item==4">周四；</span>
									<span v-if="item==5">周五；</span>
									<span v-if="item==6">周六；</span>
									<span v-if="item==7">周日；</span>
								</span>
							</div>
						</template>
					</el-table-column>
					<el-table-column prop="times" label="使用时间" width="160">
					</el-table-column>
					<el-table-column prop="teamRule" label="使用规则" width="250">
						<template slot-scope="scope">
							<el-popover placement="top-start" title="" trigger="hover">
								<div style="display: -webkit-box;-webkit-box-orient: vertical;-webkit-line-clamp: 3;overflow: hidden;max-height:80px;"
									slot="reference">
									{{scope.row.teamRule}}
								</div>
								<div style="width: 500px;height: auto;">{{scope.row.teamRule}}</div>
							</el-popover>
						</template>
					</el-table-column>
					<el-table-column prop="buyRule" label="购买须知" width="250">
						<template slot-scope="scope">
							<el-popover placement="top-start" title="" trigger="hover">
								<div style="display: -webkit-box;-webkit-box-orient: vertical;-webkit-line-clamp: 3;overflow: hidden;max-height:80px;"
									slot="reference">
									{{scope.row.buyRule}}
								</div>
								<div style="width: 500px;height: auto;">{{scope.row.buyRule}}</div>
							</el-popover>
						</template>
					</el-table-column>
					<el-table-column label="状态" fixed="right">
						<template slot-scope="scope">
							<el-switch v-model="scope.row.status" @change="change(scope.row)" :active-value="openValue"
								:inactive-value="closeValue" active-color="#13ce66" inactive-color="#ff4949">
							</el-switch>
						</template>
					</el-table-column>
					<el-table-column prop="createTime" label="创建时间" width="150">
					</el-table-column>
					<el-table-column label="操作" width="160" fixed="right">
						<template slot-scope="scope">
							<el-button style='margin:5px;' :disabled="!isAuth('coupon:add')" size="mini" type="primary"
								icon="document" @click="songCoupon(scope.row)">赠送优惠券</el-button>
							<el-button size="mini" type="primary" plain :disabled="!isAuth('coupon:update')"
								style='margin:5px;' @click="updateBtn(scope.row)">修改
							</el-button>
							<el-button size="mini" type="danger" :disabled="!isAuth('couponissue:delete')"
								style='margin:5px;' @click="shopdeletes(scope.row)">删除
							</el-button>
						</template>
					</el-table-column>
				</el-table>
				<div style="text-align: center;margin-top: 10px;">
					<el-pagination @size-change="handleSizeChange" @current-change="handleCurrentChange"
						:page-sizes="[10, 20, 30, 50, 100]" :page-size="size" :current-page="page"
						layout="total,sizes, prev, pager, next" :total="tableData.totalElements">
					</el-pagination>
				</div>
			</el-tab-pane> -->
		</el-tabs>

		<!-- 修改-->
		<el-dialog title="修改" :visible.sync="dialogFormVisible" center>
				<el-form :model="form">
					<el-form-item label="状态：" :label-width="formLabelWidth">
						<el-radio-group v-model="form.status">
							<el-radio :label="1">开启</el-radio>
							<el-radio :label="2">关闭</el-radio>
						</el-radio-group>
					</el-form-item>
					<el-form-item label="限量：" :label-width="formLabelWidth">
						<el-radio-group v-model="form.isLimit">
							<el-radio :label="1">限量</el-radio>
							<el-radio :label="2">不限量</el-radio>
						</el-radio-group>
					</el-form-item>
				</el-form>
				<div slot="footer" class="dialog-footer">
					<el-button @click="dialogFormVisible = false">取 消</el-button>
					<el-button type="primary" @click="amendNoticeTo()">确 定</el-button>
				</div>
			</el-dialog>
			<!-- 赠送优惠券-->
			<el-dialog title="赠送优惠券" :visible.sync="dialogFormVisible0" center width="80%">
				<div>
					<span style="width: 200px;display: inline-block;text-align: right;">赠送类型：</span>
					<el-radio-group v-model="types">
						<el-radio :label="1">指定用户</el-radio>
						<el-radio :label="2">所有用户</el-radio>
						<el-radio :label="3">订单用户</el-radio>
					</el-radio-group>
				</div>
				<div v-if="types==1||types==3" style="margin: 10px;">
					<span style="width: 200px;display: inline-block;text-align: right;">选择用户：</span>
					<el-tag :key="j" v-for="(tag,j) in userNameList" closable :disable-transitions="false"
						@close="couponIdClose1(tag,j)">
						{{tag}}
					</el-tag>
					<el-button class="button-new-tag" size="small" @click="couponIdBtn1(couponIds)">添加</el-button>
				</div>

				<div slot="footer" class="dialog-footer">
					<el-button @click="dialogFormVisible0 = false">取 消</el-button>
					<el-button type="primary" @click="songNoticeTo()">确 定</el-button>
				</div>
			</el-dialog>
			<!-- 用户列表 -->
			<el-dialog title="用户列表" :visible.sync="dialogFormVisible5" center width="75%">
				<div style="display: inline-block;margin-top: 5px;">
					<span>邀请人邀请码：</span>
					<el-input style="width: 150px;" @keydown.enter.native="select1" clearable placeholder="请输入邀请人邀请码"
						v-model="inviterCode">
					</el-input>&nbsp;&nbsp;&nbsp;&nbsp;
				</div>
				<div style="display: inline-block;margin-top: 5px;">
					<span>手机号：</span>
					<el-input style="width: 150px;" @keydown.enter.native="select1" clearable placeholder="请输入手机号"
						v-model="phone">
					</el-input>&nbsp;&nbsp;&nbsp;&nbsp;
				</div>
				<div style="display: inline-block;margin-top: 5px;">
					<span>昵称：</span>
					<el-input style="width: 150px;" @keydown.enter.native="select1" clearable placeholder="请输入昵称"
						v-model="nickName">
					</el-input>&nbsp;&nbsp;&nbsp;
				</div>
				<!-- <div style="display: inline-block;margin-top: 5px;">
					<span>是否是会员：</span>
					<el-select v-model="member" style="width:150px;margin-left: 10px;" @change="select1(member)">
						<el-option v-for="item in memberList" :key="item.value" :label="item.title" :value="item.value">
						</el-option>
					</el-select>&nbsp;&nbsp;&nbsp;
				</div> -->
				<div style="margin:5px;display: inline-block;">
					<span>开始时间：</span>
					<el-date-picker style="width: 160px;margin-left: 10px;" v-model="startTime" align="right"
						type="date" format="yyyy-MM-dd" value-format="yyyy-MM-dd" placeholder="选择开始时间">
					</el-date-picker>&nbsp;&nbsp;&nbsp;
				</div>
				<div style="margin:5px;position: relative;display: inline-block;">
					<span>截止时间：</span>
					<el-date-picker style="width: 160px;margin-left: 10px;" v-model="endTime" align="right" type="date"
						format="yyyy-MM-dd" value-format="yyyy-MM-dd" placeholder="选择截止时间">
					</el-date-picker>
				</div>
				<div style="margin:5px;display: inline-block;" v-if="types==3">
					<span>下单开始时间：</span>
					<el-date-picker style="width: 160px;margin-left: 10px;" v-model="ordersStartTime" align="right"
						type="date" format="yyyy-MM-dd" value-format="yyyy-MM-dd" placeholder="选择开始时间">
					</el-date-picker>&nbsp;&nbsp;&nbsp;
				</div>
				<div style="margin:5px;position: relative;display: inline-block;"  v-if="types==3">
					<span>下单截止时间：</span>
					<el-date-picker style="width: 160px;margin-left: 10px;" v-model="ordersEndTime" align="right" type="date"
						format="yyyy-MM-dd" value-format="yyyy-MM-dd" placeholder="选择截止时间">
					</el-date-picker>
				</div>
				<div style="display: inline-block;margin-top: 5px;">
					<el-button style="margin-left:15px;" size="mini" type="primary" icon="document" @click="select1">查询
					</el-button>
					<el-button style="margin-left:15px;" size="mini" type="primary" icon="document" @click="cleans1">重置
					</el-button>
				</div>
				<el-button style="margin-left:15px;" :disabled="xzList.length <= 0 " size="mini" type="primary"
					icon="document" @click="closes1()">确定</el-button>
				<el-table v-loading="tableDataLoading1" @selection-change="changeFun1" :data="userData.list">
					<el-table-column type="selection" fixed>
					</el-table-column>
					<el-table-column fixed prop="userId" label="id" width="80">
					</el-table-column>
					<el-table-column fixed prop="userName" label="昵称" width="120">
						<template slot-scope="scope">
							<span style="color: #f56c6c;"
								@click="updates(scope.row)">{{ scope.row.userName ? scope.row.userName : '未设置' }}</span>
						</template>
					</el-table-column>
					<el-table-column label="图像">
						<template slot-scope="scope">
							<img v-if="scope.row.avatar==null" src="~@/assets/img/avatar.png" alt="" width="40"
								height="40">
							<img v-else :src="scope.row.avatar" alt="" width="40" height="40">
						</template>
					</el-table-column>
					<el-table-column prop="phone" label="手机号">
						<template slot-scope="scope">
							<span style="color: #5cc477;cursor: pointer;" @click="updates(scope.row)">
								{{ scope.row.phone ? scope.row.phone : '未绑定' }}
							</span>
						</template>
					</el-table-column>
					<el-table-column prop="phone" label="邀请人邀请码">
						<template slot-scope="scope">
							<span>{{ scope.row.inviterCode ? scope.row.inviterCode : '-' }}</span>
						</template>
					</el-table-column>
					<el-table-column prop="createTime" label="创建时间" width="160"></el-table-column>
					<!-- <el-table-column prop="isVip" label="是否是会员">
						<template slot-scope="scope">
							<span v-if="scope.row.isVip==1">是</span>
							<span v-else>否</span>
						</template>
					</el-table-column> -->
				</el-table>
				<div style="text-align: center;margin-top: 10px;">
					<el-pagination @size-change="handleSizeChange2" @current-change="handleCurrentChange2"
						:page-sizes="[10, 20, 30, 50, 100]" :page-size="size1" :current-page="page1"
						layout="total,sizes, prev, pager, next,jumper" :total="userData.totalCount">
					</el-pagination>
				</div>
			</el-dialog>
			<!-- 定时赠送弹框 -->
			<el-dialog title="定时赠送弹框" :visible.sync="dialogFormVisible6" center>
				<div style="margin:5px;display: inline-block;">
					<span>赠送时间：</span>
					<el-date-picker style="width: 160px;margin-left: 10px;" v-model="startTime" align="right"
						type="datetime" format="yyyy-MM-dd HH:mm:ss" value-format="yyyy-MM-dd HH:mm:ss"
						placeholder="选择赠送时间">
					</el-date-picker>&nbsp;&nbsp;&nbsp;
				</div>
				<div slot="footer" class="dialog-footer">
					<el-button @click="dialogFormVisible6 = false">取 消</el-button>
					<el-button type="primary" @click="zengsongNotice()">确 定</el-button>
				</div>
			</el-dialog>
			<!-- 赠送优惠券-->
			<el-dialog title="修改优惠券" :visible.sync="dialogFormVisibleUp" center width="80%">
				<div style="margin-bottom: 10px;">
					<span style="width: 200px;display: inline-block;text-align: right;">选择充电站：</span>
					<el-select v-model="couponData.tearoomId" placeholder="请选择充电站" style="width:50%;"
						@change="shopClick()">
						<el-option v-for="item in shopData" :key="item.tearoomId" :label="item.tearoomName"
							:value="item.tearoomId">
						</el-option>
					</el-select>
				</div>
				<div style="margin-bottom: 10px;" v-if="couponData.couponType==1">
					<span style="width: 200px;display: inline-block;text-align: right;">选择充电桩：</span>
					<el-checkbox-group v-model="couponData.goodsIds" style="display: inline-block;">
						<el-checkbox v-for="(item,index) in goods_list" :key="index"
							:label="item.goodsId">{{item.goodsName}}</el-checkbox>
					</el-checkbox-group>
				</div>
				<div style="margin-bottom: 10px;">
					<span style="width: 200px;display: inline-block;text-align: right;">优惠券ID：</span>
					<el-input style="width:50%;" v-model="couponData.couponId" disabled></el-input>
				</div>
				<div style="margin-bottom: 10px;">
					<span style="width: 200px;display: inline-block;text-align: right;">优惠券名称：</span>
					<el-input style="width:50%;" v-model="couponData.couponName"></el-input>
				</div>
				<div style="margin-bottom: 10px;">
					<span style="width: 200px;display: inline-block;text-align: right;">是否限量：</span>
					<el-radio-group v-model="couponData.isLimit" @change="isLimitBtn()">
						<el-radio :label="1">限量</el-radio>
						<el-radio :label="2">不限量</el-radio>
					</el-radio-group>
				</div>
				<div style="margin-bottom: 10px;" v-if="couponData.isLimit==1">
					<span style="width: 200px;display: inline-block;text-align: right;">发布数量：</span>
					<el-input style="width:50%;" v-model="couponData.issueNumber" type="number" :min="0"
						placeholder="请输入发布数量">
					</el-input>
				</div>
				<div style="margin-bottom: 10px;">
					<span style="width: 200px;display: inline-block;text-align: right;">是否限制时间：</span>
					<el-radio-group v-model="isLimitTime">
						<el-radio :label="1">是</el-radio>
						<el-radio :label="2">否</el-radio>
					</el-radio-group>
				</div>
				<div style="margin-bottom: 10px;" v-if="isLimitTime==1">
					<span style="width: 200px;display: inline-block;text-align: right;">限制周期(可使用)：</span>
					<el-checkbox-group v-model="weeks" style="display: inline-block;">
						<el-checkbox label="1">周一</el-checkbox>
						<el-checkbox label="2">周二</el-checkbox>
						<el-checkbox label="3">周三</el-checkbox>
						<el-checkbox label="4">周四</el-checkbox>
						<el-checkbox label="5">周五</el-checkbox>
						<el-checkbox label="6">周六</el-checkbox>
						<el-checkbox label="7">周日</el-checkbox>
					</el-checkbox-group>
				</div>
				<div style="margin-bottom: 10px;" v-if="isLimitTime==1">
					<span style="width: 200px;display: inline-block;text-align: right;">使用时间：</span>
					<!-- <el-input style="width:50%;" v-model="times" type="number" :min="1" placeholder="请输入使用时间">
					</el-input> -->
					<span class="bqList" v-for="(item,index) in times" :key="index">{{item}}
						<span class="delss">
							<i class="el-icon-delete" @click="dels1(index)"></i>
						</span>
					</span>
					<el-time-picker style="width: 150px;" placeholder="起始时间" v-model="startTime" :picker-options="{
					      start: '00:00',
					      step: '00:01',
					      end: '23:59'
					    }" format="HH:mm" value-format="HH:mm">
					</el-time-picker>
					<el-time-picker style="width: 150px;" placeholder="结束时间" v-model="endTime" :picker-options="{
					      start: '00:00',
					      step: '00:01',
					      end: '23:59',
					      minTime: startTime
					    }" format="HH:mm" value-format="HH:mm">
					</el-time-picker>
					<el-button type="primary" class="tj" size="mini" icon="el-icon-edit"
						style="display: inline-block;padding: 7px;" circle @click="btnTj">
					</el-button>
				</div>
				<div style="margin-bottom: 10px;">
					<span style="width: 200px;display: inline-block;text-align: right;">状态：</span>
					<el-radio-group v-model="couponData.status">
						<el-radio :label="1">开启</el-radio>
						<el-radio :label="2">关闭</el-radio>
					</el-radio-group>
				</div>
				<div style="margin-bottom: 10px;">
					<span style="width: 200px;display: inline-block;text-align: right;">使用规则：</span>
					<el-input style="width:50%;" v-model="couponData.teamRule" type="textarea" :rows="3"
						placeholder="请输入使用规则">
					</el-input>
				</div>
				<div style="margin-bottom: 10px;">
					<span style="width: 200px;display: inline-block;text-align: right;">购买须知：</span>
					<el-input style="width:50%;" v-model="couponData.buyRule" type="textarea" :rows="3"
						placeholder="请输入购买须知">
					</el-input>
				</div>
				<div slot="footer" class="dialog-footer">
					<el-button @click="dialogFormVisibleUp = false">取 消</el-button>
					<el-button type="primary" @click="updateNoticeTo()">确 定</el-button>
				</div>
			</el-dialog>
	</div>
</template>
<script>
	export default {
		data() {
			return {
				activeName: 'second',
				size: 10,
				page: 1,
				form: {
					couponId: '',
					couponIssueId: '',
					couponName: '',
					createTime: '',
					endTime: '',
					isLimit: '',
					issueNumber: '',
					remainNumber: '',
					startTime: '',
					status: '',
					type: '',
					createTime: ''
				},
				formLabelWidth: '200px',
				tableDataLoading: false,
				dialogFormVisible: false,
				tableData: [],
				typeT: 2,
				store_nameT: '',
				coupon_nameT: '',
				openValue: 1,
				closeValue: 0,

				dialogFormVisible0: false,
				dialogFormVisible5: false,
				userData: [],
				types: '',
				userIdList: [],
				userNameList: [],
				xzList: [],
				couponIds: [],
				size1: 10,
				page1: 1,
				type: 1,
				types: '',
				phone: '',
				nickName: '',
				member: -1,
				memberList: [{
						value: -1,
						title: '全部'
					},
					{
						value: 0,
						title: '否'
					},
					{
						value: 1,
						title: '是'
					},
				],
				inviterCode: '',
				startTime: '',
				endTime: '',
				ordersStartTime:'',
				ordersEndTime:'',
				tableDataLoading1: false,
				couponType: '',
				checkBoxData: [],
				dialogFormVisible6: false,
				couponData: {},
				shopData: [],
				goods_list: [],
				goods_ids: [],
				dialogFormVisibleUp: false,
				store_id: '',
				isLimitTime: 1,
				weeks: [],
				times: [],

			}
		},
		methods: {
			handleClick(tab, event) {
				if (tab._props.label == '团购券') {
					this.page = 1
					this.size = 10
					this.typeT = 1
					this.dataSelect()
				}
				if (tab._props.label == '积分兑换券') {
					this.page = 1
					this.size = 10
					this.typeT = 2
					this.dataSelect()
				}
				if (tab._props.label == '新人券') {
					this.page = 1
					this.size = 10
					this.typeT = 3
					this.dataSelect()
				}
				if (tab._props.label == '会员券') {
					this.page = 1
					this.size = 10
					this.typeT = 4
					this.dataSelect()
				}

			},
			handleSizeChange(val) {
				this.size = val;
				this.dataSelect()
			},
			handleCurrentChange(val) {
				this.page = val;
				this.dataSelect()
			},
			handleSizeChange2(val) {
				this.size1 = val;
				this.dataSelect1()
			},
			handleCurrentChange2(val) {
				this.page1 = val;
				this.dataSelect1()
			},
			// 刷新
			refresh() {
				this.page = 1
				this.dataSelect()
			},
			// 重置
			cleans() {
				this.page = 1
				this.store_nameT = ''
				this.coupon_nameT = ''
				this.dataSelect()
			},
			// 删除优惠券
			shopdeletes(row) {
				this.$confirm(`确定删除此条信息?`, '提示', {
					confirmButtonText: '确定',
					cancelButtonText: '取消',
					type: 'warning'
				}).then(() => {
					this.$http({
						url: this.$http.adornUrl('selfCouponIssue/delete'),
						method: 'get',
						params: this.$http.adornParams({
							'id': row.couponIssueId
						})
					}).then(({
						data
					}) => {
						if (data && data.status == 0) {
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
								type: 'error',
								duration: 1500,
								onClose: () => {
									this.dataSelect()
								}
							})
						}
					})
				}).catch(() => {})
			},
			// 修改
			amend(index, row) {
				console.log(row);
				this.dialogFormVisible = true
				this.form.couponId = row.coupon_id
				this.form.couponIssueId = row.coupon_issue_id
				this.form.isLimit = row.is_limit
				this.form.status = row.status
			},
			// 修改
			amendNoticeTo() {
				if (this.form.status == '') {
					this.$notify({
						title: '提示',
						duration: 1800,
						message: '请选择状态',
						type: 'warning'
					});
					return
				}
				this.$http({
					url: this.$http.adornUrl2('/admin/coupon/edit_fabu_coupon'),
					method: 'post',
					data: this.$http.adornData({
						'fabu_coupon_id': this.form.couponIssueId,
						'is_limit': this.form.isLimit,
						'status': this.form.status,
					})
				}).then(({
					data
				}) => {
					this.$message({
						message: '操作成功',
						type: 'success',
						duration: 1500,
						onClose: () => {
							this.dataSelect()
						}
					})
					this.dialogFormVisible = false
				})
			},
			//获取已发布优惠券数据
			dataSelect() {
				this.tableDataLoading = true
				this.$http({
					url: this.$http.adornUrl('selfCouponIssue/list'),
					method: 'get',
					params: this.$http.adornParams({
						'page': this.page - 1,
						'size': this.size,
						'couponName': this.coupon_nameT,
						'tearoomName': this.store_nameT,
						'type': this.typeT,
					})
				}).then(({
					data
				}) => {
					this.tableDataLoading = false
					let returnData = data.data;
					this.tableData = returnData
				})
			},
			// 上下架
			change(row) {
				this.$http({
					url: this.$http.adornUrl('selfCouponIssue/update'),
					method: 'post',
					data: this.$http.adornData({
						'couponIssueId': row.couponIssueId,
						'status': row.status,
						'buyRule': row.buyRule,
						'coupon': row.coupon,
						'couponId': row.couponId,
						'couponName': row.couponName,
						'couponType': row.couponType,
						'createTime': row.createTime,
						'getStatus': row.getStatus,
						'goodsIds': row.goodsIds,
						'goodsList': row.goodsList,
						'hour': row.hour,
						'integral': row.integral,
						'isLimit': row.isLimit,
						'issueNumber': row.issueNumber,
						'minHour': row.minHour,
						'remainNumber': row.remainNumber,
						'teamRule': row.teamRule,
						'tearoom': row.tearoom,
						'tearoomId': row.tearoomId,
						'type': row.type,
						'validDay': row.validDay,
					})
				}).then(({
					data
				}) => {
					if (data && data.status == 0) {
						this.$message({
							message: '操作成功',
							type: 'success',
							duration: 1500,
						})
						this.dataSelect()
					} else {
						this.$message({
							message: data.msg,
							type: 'warning',
							duration: 1500,
						})
						this.dataSelect()
					}
				})
			},
			// 赠送优惠券
			songCoupon(row) {
				this.couponType = row.couponType
				this.userIdList = []
				this.couponIds = [row.couponIssueId]
				this.userNameList = []
				this.couponIdList = []
				// this.types = ''
				this.dialogFormVisible0 = true
			},
			// 赠送优惠券
			songNoticeTo() {

				if (this.types == '') {
					this.$notify({
						title: '提示',
						duration: 1800,
						message: '请选择赠送类型',
						type: 'warning'
					});
					return
				}
				if (this.types == 1) {
					if (this.userIdList.length == 0) {
						this.$notify({
							title: '提示',
							duration: 1800,
							message: '请选择用户',
							type: 'warning'
						});
						return
					}
				}
				if(this.types==2){
					var types = 2
				}else{
					var types = 1
				}
				var userIdList = this.userIdList.toString()
				var couponIds = this.couponIds.toString()
				this.$http({
					url: this.$http.adornUrl('selfCouponUser/sendUserCoupon'),
					method: 'post',
					params: this.$http.adornParams({
						'userIds': userIdList,
						'couponIds': couponIds,
						'type': types,
						'couponType': this.couponType,
					})
				}).then(({
					data
				}) => {
					if (data.code == 0 || data.status == 0) {
						this.$message({
							message: '操作成功',
							type: 'success',
							duration: 1500,
							onClose: () => {

								this.dataSelect()
							}
						})
						this.dialogFormVisible0 = false
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
			// 获取用户数据列表
			dataSelect1() {
				this.tableDataLoading1 = true
				this.$http({
					url: this.$http.adornUrl('user/selectUserList'),
					method: 'get',
					params: this.$http.adornParams({
						'page': this.page1,
						'limit': this.size1,
						'member': this.member,
						'userName': this.nickName,
						'inviterCode': this.inviterCode,
						'startTime': this.startTime,
						'endTime': this.endTime,
						'isAuthentication': '',
						'phone': this.phone,
						'ordersStartTime': this.ordersStartTime,
						'ordersEndTime': this.ordersEndTime,
						
						'isRate': '',
						'isVip': '',
						'moneyUser': '',
						'vipIndex':'',
						'vipNo': '',
						'isBirthday': '',
						'birthdayMonth':'',
						'integralNum':'',
						
						
					})
				}).then(({
					data
				}) => {
					this.tableDataLoading1 = false
					let returnData = data.data
					this.userData = returnData
				})
			},
			// 查询
			select1() {
				this.page = 1
				this.dataSelect1()
			},
			// 重置
			cleans1() {
				this.startTime = ''
				this.endTime = ''
				this.page = 1
				this.phone = ''
				this.member = -1
				this.nickName = ''
				this.inviterCode = ''
				this.ordersStartTime= ''
				this.ordersEndTime= ''
				this.dataSelect1()
			},
			// 确定选中
			queding(row) {
				this.userName = row.userName
				this.userId = row.userId
				this.dialogFormVisible2 = false
			},
			// 打开用户列表
			couponIdBtn1(rows) {
				this.startTime = ''
				this.endTime = ''
				this.page = 1
				this.phone = ''
				this.member = -1
				this.nickName = ''
				this.inviterCode = ''
				this.ordersStartTime= ''
				this.ordersEndTime= ''
				this.dataSelect1()

				this.dialogFormVisible5 = true
			},
			// 删除用户
			couponIdClose1(ietm, index) {
				console.log('index', index, 'ietm', ietm, this.userIdList.length)
				if (this.userIdList.length < 2) {
					this.$message({
						title: '提示',
						type: 'error',
						duration: 1800,
						message: '最少需要选中一个用户',
						type: 'warning'
					});
					return
				} else {
					this.userIdList.splice(index, 1)
					this.userNameList.splice(index, 1)
				}
			},
			// 多选用户
			changeFun1(val) {
				console.log('多选', val)
				this.xzList = val
			},
			closes1(id) {
				this.isCoupon = 1
				for (var i in this.xzList) {
					this.userIdList.push(this.xzList[i].userId)
					this.userNameList.push(this.xzList[i].userName)
				}
				this.dialogFormVisible5 = false
			},
			changeFunzs(val) {
				this.checkBoxData = val
			},
			// 定时赠送 弹框
			zengsongBtn() {
				this.dialogFormVisible6 = true
			},
			// 确实 定时赠送
			zengsongNotice() {
				this.dialogFormVisible6 = false
			},
			// 修改
			updateBtn(row) {
				this.couponData = row
				if (row.goodsIds) {
					this.couponData.goodsIds = this.couponData.goodsIds.split(',')
					for (var i in this.couponData.goodsIds) {
						this.couponData.goodsIds[i] = Number(this.couponData.goodsIds[i])
					}
				} else {
					this.couponData.goodsIds = []
				}
				this.couponData.tearoomId = Number(this.couponData.tearoomId)
				if (row.isLimitTime) {
					this.isLimitTime = row.isLimitTime
				} else {
					this.isLimitTime = 1
				}
				if (row.weeks) {
					this.weeks = row.weeks.split(',')
				} else {
					this.weeks = []
				}
				if (row.times) {
					this.times = row.times.split(',')
				} else {
					this.times = []
				}
				this.startTime = ''
				this.endTime = ''
				this.shopSelect()
				this.shopClick()
				this.dialogFormVisibleUp = true
			},
			// 获取充电站
			shopSelect() {
				this.$http({
					url: this.$http.adornUrl('tearoom/selectTearoomList'),
					method: 'get',
					params: this.$http.adornParams({
						'page': 1,
						'limit': 1000,
						'tearoomName': '',
						'city': '',
						'status': '',
					})
				}).then(({
					data
				}) => {
					let returnData = data.data.list;
					this.shopData = returnData
				})
			},
			// 获取 桌台
			shopClick() {
				this.$http({
					url: this.$http.adornUrl('goods/selectGoodsList'),
					method: 'get',
					params: this.$http.adornParams({
						'page': 1,
						'limit': 100,
						'goodsName': '',
						'status': '',
						'tearoomId': this.couponData.tearoomId,
					})
				}).then(({
					data
				}) => {
					let returnData = data.data;
					this.goods_list = returnData.list
				})
			},
			// 修改 优惠券
			updateNoticeTo() {
				if (this.isLimitTime == 1) {
					if (this.weeks.length == 0) {
						this.$notify({
							title: '提示',
							duration: 1800,
							message: '请选择限制周期',
							type: 'warning'
						});
						return
					}
					if (this.times.length == 0) {
						this.$notify({
							title: '提示',
							duration: 1800,
							message: '请添加使用时间',
							type: 'warning'
						});
						return
					}
				} else {
					this.weeks = []
					this.times = []
				}
				if (this.couponData.issueNumber === '') {
					this.$notify({
						title: '提示',
						duration: 1800,
						message: '请输入发布数量',
						type: 'warning'
					});
					return
				}
				if (this.couponData.teamRule == '') {
					this.$notify({
						title: '提示',
						duration: 1800,
						message: '请输入使用规则',
						type: 'warning'
					});
					return
				}
				if (this.couponData.buyRule == '') {
					this.$notify({
						title: '提示',
						duration: 1800,
						message: '请输入购买须知',
						type: 'warning'
					});
					return
				}
				this.couponData.goodsIds = this.couponData.goodsIds.toString()
				this.couponData.isLimitTime = this.isLimitTime
				this.couponData.weeks =  this.weeks.toString()
				this.couponData.times =  this.times.toString()
				this.$http({
					url: this.$http.adornUrl('selfCouponIssue/update'),
					method: 'post',
					data: this.$http.adornData(this.couponData)
				}).then(({
					data
				}) => {
					if (data.status == 0) {
						this.$message({
							message: '操作成功',
							type: 'success',
							duration: 1500,
							onClose: () => {
								this.dataSelect()
							}
						})
						this.dialogFormVisibleUp = false
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
			btnTj() {
				if (this.startTime == '' || this.startTime == ' ') {
					this.$notify({
						title: '提示',
						duration: 1800,
						message: '请选择开始时间',
						type: 'warning'
					});
					return
				}
				if (this.endTime == '' || this.endTime == ' ') {
					this.$notify({
						title: '提示',
						duration: 1800,
						message: '请选择结束时间',
						type: 'warning'
					});
					return
				}
				var time1 = this.startTime + '-' + this.endTime
			
				this.times.push(time1)
				this.startTime = ''
				this.endTime = ''
			},
			// 删除时间
			dels1(index) {
				this.times.splice(index, 1);
				console.log(this.times)
			},
		},
		mounted() {
			this.dataSelect()
		}
	};
</script>

<style scoped>
.couponissue-page__filter-input {
	width: 160px;
}

.couponissue-page__filter-actions .el-button + .el-button {
	margin-left: 12px;
}

.couponissue-page__tabs {
	margin-bottom: 0;
}

.couponissue-page__tabs >>> .el-tabs__header {
	margin-bottom: 0;
}

.couponissue-page__tabs .nf-table-card {
	margin-bottom: 0;
}

.couponissue-page__tabs >>> .el-tabs__item.is-active {
	color: #5cc477;
}

.couponissue-page__tabs >>> .el-tabs__active-bar {
	background-color: #5cc477;
}

.couponissue-page__pagination {
	display: flex;
	justify-content: center;
	padding-top: 12px;
}
</style>

<style>
	.divhove {
		position: absolute;
		width: 100%;
		height: 100%;
		left: 0;
		top: 0;
		opacity: 0;
		cursor: default;
		text-align: center;
		padding-top: 50%;
		border-radius: 6px;
		background-color: rgba(0, 0, 0, .5);
		-webkit-transition: opacity .3s;
		transition: opacity .3s;
	}

	.el-tag--medium {
		margin-right: 10px;
	}

	.divhove:hover {
		opacity: 0.8;
	}

	.bqList {
		padding: 4px 14px;
		margin: 4px;
		border: 1px solid #efefef;
		font-size: 12px;
		color: #999;
		border-radius: 4px;
		margin-right: 15px;
	}

	.delss {
		display: none;
		position: relative;
	}

	.delss .el-icon-delete {
		position: absolute;
		top: 0;
	}

	.bqList:hover .delss {
		display: initial;
		opacity: 0.5;
	}

	.tj {
		padding: 6px !important;
		margin: 4px;
		font-size: 12px;
		border: 1px solid #ccc;
		border-radius: 4px;
	}
</style>