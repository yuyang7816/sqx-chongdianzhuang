<template>
	<div class="nf-page coupon-page">
		<div class="nf-page-hero">
			<span class="nf-page-hero__badge">优惠券制作</span>
			<h2 class="nf-page-hero__title">优惠券制作</h2>
			<p class="nf-page-hero__desc">管理积分兑换券与新人券，支持按状态、优惠券名称筛选，可查询、重置、添加、发布与修改。</p>
			<div class="nf-page-hero__bar"></div>
		</div>

		<div class="nf-filter-card">
			<div class="nf-filter-card__head">
				<i class="el-icon-search nf-filter-card__head-icon"></i>
				<span>筛选条件</span>
			</div>
			<el-form :inline="true" class="nf-filter-card__body" @submit.native.prevent>
				<el-form-item label="状态">
					<el-select
						class="coupon-page__filter-select"
						v-model="statusT"
						placeholder="请选择状态"
						@change="refresh()">
						<el-option v-for="item in statusmain" :key="item.value" :label="item.label" :value="item.value">
						</el-option>
					</el-select>
				</el-form-item>
				<el-form-item label="优惠券名称">
					<el-input
						class="coupon-page__filter-input"
						@keydown.enter.native="refresh"
						clearable
						placeholder="请输入优惠券名称"
						v-model="coupon_nameT">
					</el-input>
				</el-form-item>
				<el-form-item class="coupon-page__filter-actions">
					<el-button type="primary" icon="el-icon-search" @click="refresh">查询</el-button>
					<el-button type="primary" icon="el-icon-refresh-left" @click="cleans">重置</el-button>
					<el-button type="primary" icon="el-icon-plus" :disabled="!isAuth('coupon:add')" @click="addCoupon">添加</el-button>
				</el-form-item>
			</el-form>
		</div>

		<el-tabs v-model="activeName" @tab-click="handleClick" class="coupon-page__tabs">
			<el-tab-pane label="积分兑换券" name="first">
				<div class="nf-table-card">
				<el-table v-loading="tableDataLoading" :data="tableData.content">
					<el-table-column prop="couponId" label="编号" width="80" fixed="left">
					</el-table-column>
					<el-table-column prop="type" label="类型">
						<template slot-scope="scope">
							<span v-if="scope.row.type == 1">团购券</span>
							<span v-if="scope.row.type == 2">积分兑换券</span>
							<span v-if="scope.row.type == 3">新人券</span>
						</template>
					</el-table-column>
					<!-- <el-table-column prop="type" label="券类型" fixed="left">
						<template slot-scope="scope">
							<span v-if="scope.row.couponType == 1">优惠券</span>
							<span v-if="scope.row.couponType == 2">实物</span>
							<span v-if="scope.row.couponType == 3">定时券</span>
							
						</template>
					</el-table-column> -->
					<el-table-column prop="couponName" label="名称" width="150" fixed="left">
					</el-table-column>
					<el-table-column prop="goodsImages" label="图片">
						<template slot-scope="scope">
							<div v-if="scope.row.goodsImages">
								<el-popover placement="top-start" title="" trigger="hover">
									<img style="width: 50px; height: 50px;margin: 3px;" :src="scope.row.goodsImages"
										alt="" slot="reference">
									<img style="width: 300px; height: auto" :src="scope.row.goodsImages" alt="">
								</el-popover>
							</div>
						</template>
					</el-table-column>
					<el-table-column prop="integral" label="兑换积分">
					</el-table-column>
					<el-table-column prop="lessMoney" label="现价">
					</el-table-column>
					<el-table-column prop="minMoney" label="原价">
					</el-table-column>
					<!-- <el-table-column prop="hour" label="使用时长(小时)">
						<template slot-scope="scope">
							<span v-if="scope.row.couponType == 1">{{scope.row.hour}}</span>
							<span v-if="scope.row.couponType == 2">-</span>
						</template>
					</el-table-column>
					<el-table-column prop="minHour" label="最低使用时长(小时)">
						<template slot-scope="scope">
							<span v-if="scope.row.couponType == 1">{{scope.row.minHour}}</span>
							<span v-if="scope.row.couponType == 2">-</span>
						</template>
					</el-table-column> -->
					
					<el-table-column prop="validDay" label="有效期限(天)">
					</el-table-column>
					<el-table-column prop="sort" label="排序">
					</el-table-column>
					<el-table-column label="状态" fixed="right" width="80">
						<template slot-scope="scope">
							<span class="coupon-page__status-on" v-if="scope.row.status == 1">开启</span>
							<span v-if="scope.row.status == 2">关闭</span>
						</template>
					</el-table-column>
					<el-table-column prop="createTime" label="创建时间" width="150">
					</el-table-column>
					<el-table-column label="操作" width="150" fixed="right">
						<template slot-scope="scope">
							<el-button size="mini" type="primary" :disabled="!isAuth('coupon:issue')"
								style="margin: 3px;" @click="discuss(scope.$index,scope.row)">发布
							</el-button>
							<el-button size="mini" type="primary" :disabled="!isAuth('coupon:update')"
								style="margin: 3px;" @click="shopUpdates(scope.$index,scope.row)">修改
							</el-button>
							<!-- <el-button size="mini" type="danger" :disabled="!isAuth('coupon:delete')" style="margin: 3px;"
								@click="shopdeletes(scope.row)">删除
							</el-button> -->
						</template>
					</el-table-column>
				</el-table>
				<div class="coupon-page__pagination">
					<el-pagination @size-change="handleSizeChange" @current-change="handleCurrentChange"
						:page-sizes="[10, 20, 30, 50, 100]" :page-size="size" :current-page="page"
						layout="total,sizes, prev, pager, next" :total="tableData.totalElements">
					</el-pagination>
				</div>
				</div>
			</el-tab-pane>
			<!-- <el-tab-pane label="团购券" name="second">
				<el-table v-loading="tableDataLoading" :data="tableData.content">
					<el-table-column prop="couponId" label="编号" width="80" fixed="left">
					</el-table-column>
					<el-table-column prop="type" label="类型">
						<template slot-scope="scope">
							<span v-if="scope.row.type == 1">团购券</span>
							<span v-if="scope.row.type == 2">积分兑换券</span>
							<span v-if="scope.row.type == 3">新人券</span>
						</template>
					</el-table-column>
					<el-table-column prop="type" label="券类型" fixed="left">
						<template slot-scope="scope">
							<span v-if="scope.row.couponType == 1">优惠券</span>
							<span v-if="scope.row.couponType == 2">实物</span>
							<span v-if="scope.row.couponType == 3">定时券</span>
						</template>
					</el-table-column>
					<el-table-column prop="couponName" label="名称" width="150" fixed="left">
					</el-table-column>
					<el-table-column prop="goodsImages" label="图片">
						<template slot-scope="scope">
							<div v-if="scope.row.goodsImages">
								<el-popover placement="top-start" title="" trigger="hover">
									<img style="width: 50px; height: 50px;margin: 3px;" :src="scope.row.goodsImages"
										alt="" slot="reference">
									<img style="width: 300px; height: auto" :src="scope.row.goodsImages" alt="">
								</el-popover>
							</div>
						</template>
					</el-table-column>
					<el-table-column prop="lessMoney" label="现价">
					</el-table-column>
					<el-table-column prop="minMoney" label="原价">
					</el-table-column>
					<el-table-column prop="hour" label="使用时长(小时)">
						<template slot-scope="scope">
							<span v-if="scope.row.couponType == 1">{{scope.row.hour}}</span>
							<span v-if="scope.row.couponType == 2">-</span>
						</template>
					</el-table-column>
					<el-table-column prop="minHour" label="最低使用时长(小时)">
						<template slot-scope="scope">
							<span v-if="scope.row.couponType == 1">{{scope.row.minHour}}</span>
							<span v-if="scope.row.couponType == 2">-</span>
						</template>
					</el-table-column>
					
					<el-table-column prop="validDay" label="有效期限(天)">
					</el-table-column>
					<el-table-column prop="sort" label="排序">
					</el-table-column>
					<el-table-column label="状态" fixed="right">
						<template slot-scope="scope">
							<span style="color: #5cc477;" v-if="scope.row.status == 1">开启</span>
							<span v-if="scope.row.status == 2">关闭</span>
						</template>
					</el-table-column>
					<el-table-column prop="createTime" label="创建时间" width="150">
					</el-table-column>
					<el-table-column label="操作" width="150" fixed="right">
						<template slot-scope="scope">
							<el-button size="mini" type="primary" :disabled="!isAuth('coupon:issue')"
								style="margin: 3px;" @click="discuss(scope.$index,scope.row)">发布
							</el-button>
							<el-button size="mini" type="primary" :disabled="!isAuth('coupon:update')"
								style="margin: 3px;" @click="shopUpdates(scope.$index,scope.row)">修改
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
			<el-tab-pane label="新人券" name="three">
				<div class="nf-table-card">
				<el-table v-loading="tableDataLoading" :data="tableData.content">
					<el-table-column prop="couponId" label="编号" width="80" fixed="left">
					</el-table-column>
					<el-table-column prop="type" label="类型">
						<template slot-scope="scope">
							<span v-if="scope.row.type == 1">团购券</span>
							<span v-if="scope.row.type == 2">积分兑换券</span>
							<span v-if="scope.row.type == 3">新人券</span>
						</template>
					</el-table-column>
					<<!-- el-table-column prop="type" label="券类型" fixed="left">
						<template slot-scope="scope">
							<span v-if="scope.row.couponType == 1">优惠券</span>
							<span v-if="scope.row.couponType == 2">实物</span>
							<span v-if="scope.row.couponType == 3">定时券</span>
						</template>
					</el-table-column> -->
					<el-table-column prop="couponName" label="名称" width="150" fixed="left">
					</el-table-column>
					<el-table-column prop="goodsImages" label="图片">
						<template slot-scope="scope">
							<div v-if="scope.row.goodsImages">
								<el-popover placement="top-start" title="" trigger="hover">
									<img style="width: 50px; height: 50px;margin: 3px;" :src="scope.row.goodsImages"
										alt="" slot="reference">
									<img style="width: 300px; height: auto" :src="scope.row.goodsImages" alt="">
								</el-popover>
							</div>
						</template>
					</el-table-column>
					<el-table-column prop="lessMoney" label="面值">
					</el-table-column>
					<el-table-column prop="minMoney" label="最低消费金额">
					</el-table-column>
					<!-- <el-table-column prop="hour" label="使用时长(小时)">
						<template slot-scope="scope">
							<span v-if="scope.row.couponType == 1">{{scope.row.hour}}</span>
							<span v-if="scope.row.couponType == 2">-</span>
						</template>
					</el-table-column>
					<el-table-column prop="minHour" label="最低使用时长(小时)">
						<template slot-scope="scope">
							<span v-if="scope.row.couponType == 1">{{scope.row.minHour}}</span>
							<span v-if="scope.row.couponType == 2">-</span>
						</template>
					</el-table-column> -->
					
					<el-table-column prop="validDay" label="有效期限(天)">
					</el-table-column>
					<el-table-column prop="sort" label="排序">
					</el-table-column>
					<el-table-column label="状态" fixed="right">
						<template slot-scope="scope">
							<span class="coupon-page__status-on" v-if="scope.row.status == 1">开启</span>
							<span v-if="scope.row.status == 2">关闭</span>
						</template>
					</el-table-column>
					<el-table-column prop="createTime" label="创建时间" width="150">
					</el-table-column>
					<el-table-column label="操作" width="150" fixed="right">
						<template slot-scope="scope">
							<el-button size="mini" type="primary" :disabled="!isAuth('coupon:issue')"
								style="margin: 3px;" @click="discuss(scope.$index,scope.row)">发布
							</el-button>
							<el-button size="mini" type="primary" :disabled="!isAuth('coupon:update')"
								style="margin: 3px;" @click="shopUpdates(scope.$index,scope.row)">修改
							</el-button>
							<!-- <el-button size="mini" type="danger" :disabled="!isAuth('coupon:delete')" style="margin: 3px;"
								@click="shopdeletes(scope.row)">删除
							</el-button> -->
						</template>
					</el-table-column>
				</el-table>
				<div class="coupon-page__pagination">
					<el-pagination @size-change="handleSizeChange" @current-change="handleCurrentChange"
						:page-sizes="[10, 20, 30, 50, 100]" :page-size="size" :current-page="page"
						layout="total,sizes, prev, pager, next" :total="tableData.totalElements">
					</el-pagination>
				</div>
				</div>
			</el-tab-pane>
			<!-- <el-tab-pane label="会员劵" name="four">
				<el-table v-loading="tableDataLoading" :data="tableData.content">
					<el-table-column prop="couponId" label="编号" width="80" fixed="left">
					</el-table-column>
					<el-table-column prop="type" label="类型">
						<template slot-scope="scope">
							<span v-if="scope.row.type == 1">团购券</span>
							<span v-if="scope.row.type == 2">积分兑换券</span>
							<span v-if="scope.row.type == 3">新人券</span>
							<span v-if="scope.row.type == 4">会员劵</span>
						</template>
					</el-table-column>
					<el-table-column prop="type" label="券类型" fixed="left">
						<template slot-scope="scope">
							<span v-if="scope.row.couponType == 1">优惠券</span>
							<span v-if="scope.row.couponType == 2">实物</span>
							<span v-if="scope.row.couponType == 3">定时券</span>
						</template>
					</el-table-column>
					<el-table-column prop="couponName" label="名称" width="150" fixed="left">
					</el-table-column>
					<el-table-column prop="goodsImages" label="图片">
						<template slot-scope="scope">
							<div v-if="scope.row.goodsImages">
								<el-popover placement="top-start" title="" trigger="hover">
									<img style="width: 50px; height: 50px;margin: 3px;" :src="scope.row.goodsImages"
										alt="" slot="reference">
									<img style="width: 300px; height: auto" :src="scope.row.goodsImages" alt="">
								</el-popover>
							</div>
						</template>

					</el-table-column>
					<el-table-column prop="lessMoney" label="现价">
					</el-table-column>
					<el-table-column prop="minMoney" label="原价">
					</el-table-column>
					<el-table-column prop="hour" label="使用时长(小时)">
						<template slot-scope="scope">
							<span v-if="scope.row.couponType == 1">{{scope.row.hour}}</span>
							<span v-if="scope.row.couponType == 2">-</span>
						</template>
					</el-table-column>
					<el-table-column prop="minHour" label="最低使用时长(小时)">
						<template slot-scope="scope">
							<span v-if="scope.row.couponType == 1">{{scope.row.minHour}}</span>
							<span v-if="scope.row.couponType == 2">-</span>
						</template>
					</el-table-column>
					
					<el-table-column prop="validDay" label="有效期限(天)">
					</el-table-column>
					<el-table-column prop="sort" label="排序">
					</el-table-column>
					<el-table-column label="状态" fixed="right">
						<template slot-scope="scope">
							<span style="color: #5cc477;" v-if="scope.row.status == 1">开启</span>
							<span v-if="scope.row.status == 2">关闭</span>
						</template>
					</el-table-column>
					<el-table-column prop="createTime" label="创建时间" width="150">
					</el-table-column>
					<el-table-column label="操作" width="150" fixed="right">
						<template slot-scope="scope">
							<el-button size="mini" type="primary" :disabled="!isAuth('coupon:issue')"
								style="margin: 3px;" @click="discuss(scope.$index,scope.row)">发布
							</el-button>
							<el-button size="mini" type="primary" :disabled="!isAuth('coupon:update')"
								style="margin: 3px;" @click="shopUpdates(scope.$index,scope.row)">修改
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

		<!-- 添加优惠券-->
		<el-dialog title="添加优惠券" :visible.sync="dialogFormVisible" center width="80%">
				<div style="margin-bottom: 10px;">
					<span style="width: 200px;display: inline-block;text-align: right;">类型：</span>
					<el-radio-group v-model="type" @change="couponClick">
						<!-- <el-radio :label="1">团购券</el-radio> -->
						<el-radio :label="2">积分兑换券</el-radio>
						<el-radio :label="3">新人券</el-radio>
						<!-- <el-radio :label="4">会员劵</el-radio> -->
					</el-radio-group>
				</div>
				<!-- <div style="margin-bottom: 10px;">
					<span style="width: 200px;display: inline-block;text-align: right;">券类型：</span>
					<el-radio-group v-model="couponType" @change="couponTypeBtn()">
						<el-radio :label="1">优惠券</el-radio>
						<el-radio :label="2" v-if="type!=3">实物</el-radio>
						<el-radio :label="3">定时券</el-radio>
					</el-radio-group>
				</div> -->
				<div style="margin-bottom: 10px;">
					<span style="width: 200px;display: inline-block;text-align: right;">名称：</span>
					<el-input style="width:50%;" v-model="couponName" placeholder="请输入名称"></el-input>
				</div>
				<div style="margin-bottom: 10px;">
					<span style="width: 200px;display: inline-block;text-align: right;">现价/面值：</span>
					<el-input style="width:50%;" v-model="lessMoney" type="number" :min="0" placeholder="请输入现价/面值">
					</el-input>
				</div>
				<div style="margin-bottom: 10px;">
					<span style="width: 200px;display: inline-block;text-align: right;">原价/最低消费金额：</span>
					<el-input style="width:50%;" v-model="minMoney" type="number" :min="0" placeholder="请输入原价/最低消费金额">
					</el-input>
				</div>
				<div style="margin-bottom: 10px;" v-if="type==2">
					<span style="width: 200px;display: inline-block;text-align: right;">兑换积分：</span>
					<el-input style="width:50%;" v-model="integral" type="number" :min="0" placeholder="请输入兑换积分">
					</el-input>
				</div>

			<!-- 	<div style="margin-bottom: 10px;" v-if="couponType==1">
					<span style="width: 200px;display: inline-block;text-align: right;">使用时长(小时)：</span>
					<el-input style="width:50%;" v-model="used_time" type="number" :min="1" placeholder="请输入使用时长(小时)">
					</el-input>
				</div>
				<div style="margin-bottom: 10px;" v-if="couponType==1">
					<span style="width: 200px;display: inline-block;text-align: right;">是否限制最低使用时长：</span>
					<el-radio-group v-model="minHourType" @change="minHourBtn()">
						<el-radio :label="1">是</el-radio>
						<el-radio :label="2">否</el-radio>
					</el-radio-group>
				</div> -->
				<!-- <div style="margin-bottom: 10px;" v-if="couponType==1&&minHourType==1">
					<span style="width: 200px;display: inline-block;text-align: right;">最低使用时长(小时)：</span>
					<el-input style="width:50%;" v-model="minHour" type="number" :min="1" placeholder="请输入最低使用时长(小时)">
					</el-input>
				</div> -->
				
				<div style="margin-bottom: 10px;">
					<span style="width: 200px;display: inline-block;text-align: right;">有效期限(天)：</span>
					<el-input style="width:50%;" v-model="validDay" type="number" :min="1" placeholder="请输入有效期限(天)">
					</el-input>
				</div>
				<div style="margin-bottom: 10px;">
					<span style="width: 200px;display: inline-block;text-align: right;">排序：</span>
					<el-input style="width:50%;" v-model="sort" type="number" :min="0" placeholder="请输入排序号"></el-input>
				</div>
				<div style="margin-bottom: 10px;">
					<span style="width: 200px;display: inline-block;text-align: right;">状态：</span>
					<el-radio-group v-model="status">
						<el-radio :label="1">开启</el-radio>
						<el-radio :label="2">关闭</el-radio>
					</el-radio-group>
				</div>
				<div style="margin-bottom: 10px;display: flex;align-items: center;">
					<span style="width: 200px;display: inline-block;text-align: right;">图片：</span>
					<div
						style="width:148px;height:148px;background-color: #fbfdff; border: 1px dashed #c0ccda;border-radius: 6px;text-align: center;line-height: 148px;">
						<el-upload class="avatar-uploader" v-model="goodsImages"
							:action="$http.adornUrlUp('alioss/upload')" :show-file-list="false"
							:on-success="handleAvatarSuccess">
							<img v-if="goodsImages" :src="goodsImages" class="avatar"
								style="width: 148px;height: 148px;border-radius:6px;" />
							<i v-else class="el-icon-plus avatar-uploader-icon"
								style="font-size: 28px;color: #8c939d"></i>
						</el-upload>
					</div>
				</div>
				<div slot="footer" class="dialog-footer">
					<el-button @click="dialogFormVisible = false">取 消</el-button>
					<el-button type="primary" @click="couponNoticeTo()">确 定</el-button>
				</div>
			</el-dialog>
			<!-- 修改优惠券 -->
			<el-dialog title="修改优惠券" :visible.sync="dialogFormVisible3" center width="80%">
				<el-form :model="form">
					<el-form-item label="类型：" :label-width="formLabelWidth">
						<el-radio-group v-model="form.type" @change="couponClick">
							<!-- <el-radio :label="1">团购券</el-radio> -->
							<el-radio :label="2">积分兑换券</el-radio>
							<el-radio :label="3">新人券</el-radio>
							<!-- <el-radio :label="4">会员劵</el-radio> -->
						</el-radio-group>
					</el-form-item>
					<!-- <el-form-item label="券类型：" :label-width="formLabelWidth">
						<el-radio-group v-model="form.couponType" @change="couponTypeBtn()">
							<el-radio :label="1">优惠券</el-radio>
							<el-radio :label="2" v-if="form.type!=3">实物</el-radio>
							<el-radio :label="3">定时券</el-radio>
						</el-radio-group>
					</el-form-item> -->
					<el-form-item label="名称：" :label-width="formLabelWidth">
						<el-input v-model="form.couponName" style="width:65%;" placeholder="请输入名称"></el-input>
					</el-form-item>
					<el-form-item label="现价/面值：" :label-width="formLabelWidth">
						<el-input v-model="form.lessMoney" style="width:65%;" placeholder="请输入现价/面值"></el-input>
					</el-form-item>
					<el-form-item label="原价/最低消费金额：" :label-width="formLabelWidth">
						<el-input v-model="form.minMoney" style="width:65%;" placeholder="请输入原价/最低消费金额"></el-input>
					</el-form-item>
					<el-form-item label="兑换积分：" :label-width="formLabelWidth" v-if="form.type==2">
						<el-input v-model="form.integral" style="width:65%;" placeholder="请输入兑换积分"></el-input>
					</el-form-item>

					<!-- <el-form-item label="使用时长(小时)：" :label-width="formLabelWidth" v-if="form.couponType==1">
						<el-input v-model="form.used_time" style="width:65%;" placeholder="请输入使用时长(小时)"></el-input>
					</el-form-item>
					<el-form-item label="是否限制最低使用时长：" :label-width="formLabelWidth" v-if="form.couponType==1">
						<el-radio-group v-model="minHourType" @change="minHourBtn()">
							<el-radio :label="1">是</el-radio>
							<el-radio :label="2">否</el-radio>
						</el-radio-group>
					</el-form-item> -->
					
					<!-- <el-form-item label="最低使用时长(小时)：" :label-width="formLabelWidth" v-if="form.couponType==1">
						<el-input v-model="form.minHour" style="width:65%;" placeholder="请输入最低使用时长(小时)"></el-input>
					</el-form-item> -->
					<el-form-item label="有效期限(天)：" :label-width="formLabelWidth">
						<el-input v-model="form.validDay" style="width:65%;" placeholder="请输入有效期限(天)"></el-input>
					</el-form-item>
					<el-form-item label="排序：" :label-width="formLabelWidth">
						<el-input v-model="form.sort" style="width:65%;" placeholder="请输入排序号"></el-input>
					</el-form-item>
					<el-form-item label="状态：" :label-width="formLabelWidth">
						<el-radio-group v-model="form.status">
							<el-radio :label="1">开启</el-radio>
							<el-radio :label="2">关闭</el-radio>
						</el-radio-group>
					</el-form-item>
					<el-form-item label="图片：" :label-width="formLabelWidth">
						<el-upload class="avatar-uploader" v-model="form.goodsImages"
							style="width:148px;height:148px;background-color: #fbfdff; border: 1px dashed #c0ccda;border-radius: 6px;text-align: center;line-height: 148px;"
							:action="$http.adornUrlUp('alioss/upload')" :show-file-list="false"
							:on-success="handleAvatarSuccessF">
							<img v-if="form.goodsImages" :src="form.goodsImages" class="avatar"
								style="width: 148px;height: 148px;border-radius:6px;" />
							<i v-else class="el-icon-plus avatar-uploader-icon"
								style="font-size: 28px;color: #8c939d"></i>
						</el-upload>
					</el-form-item>


				</el-form>
				<div slot="footer" class="dialog-footer">
					<el-button @click="dialogFormVisible3 = false">取 消</el-button>
					<el-button type="primary" @click="amendNoticeTo()">确 定</el-button>
				</div>
			</el-dialog>
			<!-- 发布优惠券-->
			<el-dialog title="发布优惠券" :visible.sync="dialogFormVisible2" center width="80%">
				<div style="margin-bottom: 10px;">
					<span style="width: 200px;display: inline-block;text-align: right;">选择充电桩：</span>
					<el-select v-model="store_id" placeholder="请选择充电桩" style="width:50%;" @change="shopClick()">
						<el-option v-for="item in shopData" :key="item.tearoomId" :label="item.tearoomName"
							:value="item.tearoomId">
						</el-option>
					</el-select>
				</div>
				<div style="margin-bottom: 10px;" v-if="couponType==1||couponType==3">
					<span style="width: 200px;display: inline-block;text-align: right;">选择充电桩：</span>
					<el-checkbox-group v-model="goods_ids" style="display: inline-block;">
						<el-checkbox v-for="(item,index) in goods_list" :key="index"
							:label="item.goodsId">{{item.goodsName}}</el-checkbox>
					</el-checkbox-group>
				</div>
				<div style="margin-bottom: 10px;">
					<span style="width: 200px;display: inline-block;text-align: right;">优惠券ID：</span>
					<el-input style="width:50%;" v-model="couponId" disabled></el-input>
				</div>
				<div style="margin-bottom: 10px;">
					<span style="width: 200px;display: inline-block;text-align: right;">优惠券名称：</span>
					<el-input style="width:50%;" v-model="couponName1"></el-input>
				</div>
				<!-- <div style="margin-bottom: 10px;">
					<span style="width: 200px;display: inline-block;text-align: right;">领取开启时间：</span>
					<el-date-picker style="width:200px;" v-model="startTime" align="right" type="datetime"
						format="yyyy-MM-dd HH:mm:ss" value-format="yyyy-MM-dd HH:mm:ss" placeholder="选择开始时间">
					</el-date-picker>
				</div>
				<div style="margin-bottom: 10px;">
					<span style="width: 200px;display: inline-block;text-align: right;">券领结束时间：</span>
					<el-date-picker style="width:200px;" v-model="endTime" align="right" type="datetime"
						format="yyyy-MM-dd HH:mm:ss" value-format="yyyy-MM-dd HH:mm:ss" placeholder="选择结束时间">
					</el-date-picker>
				</div>
				
				<div style="margin-bottom: 10px;" v-if="typeyouhijuan==3">
					<span style="width: 200px;display: inline-block;text-align: right;"> 积分数量：</span>
					<el-input style="width:50%;" v-model="jifenNumber" type="number" :min="0" placeholder="请输入积分数量">
					</el-input>
				</div>
				 -->
				<div style="margin-bottom: 10px;">
					<span style="width: 200px;display: inline-block;text-align: right;">是否限量：</span>
					<el-radio-group v-model="isLimit" @change="isLimitBtn()">
						<el-radio :label="1">限量</el-radio>
						<el-radio :label="2">不限量</el-radio>
					</el-radio-group>
				</div>
				<div style="margin-bottom: 10px;" v-if="isLimit==1">
					<span style="width: 200px;display: inline-block;text-align: right;">发布数量：</span>
					<el-input style="width:50%;" v-model="issueNumber" type="number" :min="0" placeholder="请输入发布数量">
					</el-input>
				</div>
				<div style="margin-bottom: 10px;" v-if="couponType!=3">
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
					
					<span v-if="couponType==3">
						<el-time-picker style="width: 150px;" placeholder="起始时间" v-model="startTime" :picker-options="{
						      start: '00:00',
						      step: '00:01',
						      end: '23:59'
						    }" format="HH:mm" value-format="HH:mm"  v-if="times.length==0">
						</el-time-picker>
						<el-time-picker style="width: 150px;" placeholder="结束时间" v-model="endTime" :picker-options="{
						      start: '00:00',
						      step: '00:01',
						      end: '23:59',
						      minTime: startTime
						    }" format="HH:mm" value-format="HH:mm"  v-if="times.length==0">
						</el-time-picker>
						<el-button type="primary" class="tj" size="mini" icon="el-icon-edit" v-if="times.length==0"
							style="display: inline-block;padding: 7px;" circle @click="btnTj">
						</el-button>
					</span>
					<span v-else>
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
					</span>
					
				</div>
				<div style="margin-bottom: 10px;">
					<span style="width: 200px;display: inline-block;text-align: right;">状态：</span>
					<el-radio-group v-model="status">
						<el-radio :label="1">开启</el-radio>
						<el-radio :label="2">关闭</el-radio>
					</el-radio-group>
				</div>

				<div style="margin-bottom: 10px;">
					<span style="width: 200px;display: inline-block;text-align: right;">使用规则：</span>
					<el-input style="width:50%;" v-model="teamRule" type="textarea" :rows="3" placeholder="请输入使用规则">
					</el-input>
				</div>
				<div style="margin-bottom: 10px;">
					<span style="width: 200px;display: inline-block;text-align: right;">购买须知：</span>
					<el-input style="width:50%;" v-model="buyRule" type="textarea" :rows="3" placeholder="请输入购买须知">
					</el-input>
				</div>
				<div slot="footer" class="dialog-footer">
					<el-button @click="dialogFormVisible2 = false">取 消</el-button>
					<el-button type="primary" @click="discussNoticeTo()">确 定</el-button>
				</div>
			</el-dialog>
			<!-- 选择商品弹框 -->
			<el-dialog title="商品列表" customClass="customWidth" :visible.sync="dialogFormVisible1" center>
				<div>
					<div style="margin:2% 0;display: inline-block;">
						<span>商品标题：</span>
						<el-input style="width: 150px;" @keydown.enter.native="select" clearable placeholder="请输入商品标题"
							v-model="titles"></el-input>&nbsp;&nbsp;&nbsp;&nbsp;
					</div>
					<div style="display: inline-block;">
						<el-button style='margin-left:15px;' size="mini" type="primary" icon="document"
							@click="select">查询
						</el-button>
						<el-button style='margin-left:15px;' size="mini" type="primary" icon="document"
							@click="cleans">重置
						</el-button>
					</div>
					<el-table v-loading="tableDataLoading1" :data="shopData.data">
						<el-table-column prop="id" label="编号" width="80">
						</el-table-column>
						<el-table-column prop="goods_image" label="商品图片">
							<template slot-scope="scope">
								<img :src="scope.row.goods_image" alt="" width="60" height="60">
							</template>
						</el-table-column>
						<el-table-column prop="title" label="商品标题" width="200">
						</el-table-column>
						<el-table-column prop="originalPrice" label="商品原价">
							<template slot-scope="scope">
								<span style="color: #f56c6c">{{
					  scope.row.goods_money.old_price | numFilter
					}}</span>
							</template>
						</el-table-column>
						<el-table-column prop="price" label="商品价格">
							<template slot-scope="scope">
								<span style="color: #f56c6c">{{
					  scope.row.goods_money.new_price | numFilter
					}}</span>
							</template>
						</el-table-column>
						<el-table-column prop="memberPrice" label="会员价格">
							<template slot-scope="scope">
								<span style="color: #f56c6c">{{
					  scope.row.goods_money.vip_price | numFilter
					}}</span>
							</template>
						</el-table-column>
						<el-table-column label="操作" width="150">
							<template slot-scope="scope">
								<el-button size="mini" type="primary" @click="selectClick(scope.$index,scope.row)">选择
								</el-button>
							</template>
						</el-table-column>
					</el-table>
					<div style="text-align: center;margin-top: 10px;">
						<el-pagination @size-change="handleSizeChange1" @current-change="handleCurrentChange1"
							:page-sizes="[10, 20, 30, 50, 100]" :page-size="size" :current-page="page"
							layout="total,sizes, prev, pager, next" :total="shopData.total">
						</el-pagination>
					</div>
				</div>
			</el-dialog>
	</div>
</template>

<script>
	export default {
		data() {
			return {
				activeName: 'first',
				typeyouhijuan: 1,
				jifenNumber: '',
				size: 10,
				page: 1,
				type: 2,
				typeT: 2,
				statusT: '',
				statusmain: [{
						value: '',
						label: '全部'
					},
					{
						value: 1,
						label: '开启'
					},
					{
						value: 2,
						label: '关闭'
					},
				],
				coupon_nameT: '',
				title: '',
				typeId: '',
				couponName: '',
				couponType: 1,
				lessMoney: 0,
				minMoney: 0,
				integral: 0,
				validDay: 30,
				used_time: 4,
				minHour: '',
				minHourType: 1,
				goodsImages: '',
				sort: 0,
				status: 1,
				store_id: '',
				goods_list: [],
				goods_ids: [],
				goodsIds: '',
				issueNumber: 0,
				remainNumber: '',
				isLimit: 1,
				form: {
					couponId: '',
					type: 2,
					couponName: '',
					lessMoney: '',
					goodsIds: '',
					goodsImages: '',
					minMoney: '',
					integral: '',
					validDay: '',
					used_time: '',
					sort: '',
					status: '',
					createTime: '',
					couponType: 1,
					minHour: '',
				},
				coverImg: '',
				couponName1: '',
				couponId: '',
				startTime: '',
				endTime: '',
				checkBoxData: [], //多选框选择的值
				formLabelWidth: '200px',
				tableDataLoading: false,
				tableDataLoading1: false,
				dialogFormVisible: false,
				dialogFormVisible1: false,
				dialogFormVisible2: false,
				dialogFormVisible3: false,
				tableData: [],
				shopData: [],
				titles: '',
				teamRule: '',
				buyRule: '',

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
				if (tab._props.label == '会员劵') {
					this.page = 1
					this.size = 10
					this.typeT = 4
					this.dataSelect()
				}
			},
			// 通用券/商品券
			couponClick(val) {
				if (val = 1) {
					this.goodsIds = ''
					this.goodsImages = ''
					this.form.goodsIds = ''
					this.form.goodsImages = ''
				}
			},
			// couponClick1(val) {
			//   if (val = 1) {
			//     this.goodsIds = ''
			//     this.goodsImages = ''
			//     this.form.goodsIds = ''
			//     this.form.goodsImages = ''
			//   }
			// },
			// 刷新
			refresh() {
				this.page = 1
				this.dataSelect()
			},
			// 重置
			cleans() {
				this.page = 1
				this.statusT = ''
				this.coupon_nameT = ''
				this.dataSelect()
			},
			handleSizeChange(val) {
				this.size = val;
				this.dataSelect()
			},
			handleCurrentChange(val) {
				this.page = val;
				this.dataSelect()
			},
			handleSizeChange1(val) {
				this.size = val;
				this.shopSelect()
			},
			handleCurrentChange1(val) {
				this.page = val;
				this.shopSelect()
			},
			// 添加优惠券弹框
			addCoupon() {
				this.type = this.typeT
				this.couponName = ''
				this.goodsImages = ''
				this.lessMoney = 0
				this.validDay = 30

				this.minMoney = 0
				this.integral = 0
				this.sort = 0
				this.status = 1
				this.couponType = 1
				this.minHourType = 2
				this.minHour = ''
				
				if (this.couponType == 1) {
					this.used_time = 4

				} else {
					this.used_time = ''
				}
				this.dialogFormVisible = true
			},
			// 添加优惠券
			couponNoticeTo() {
				if (this.couponName == '') {
					this.$notify({
						title: '提示',
						duration: 1800,
						message: '请输入优惠券名称',
						type: 'warning'
					});
					return
				}
				// console.log(this.lessMoney, this.minMoney);
				// if (parseFloat(this.minMoney) < parseFloat(this.lessMoney)) {
				// 	this.$notify({
				// 		title: '提示',
				// 		duration: 1800,
				// 		message: '优惠券金额不能高于最低消费金额',
				// 		type: 'warning'
				// 	});
				// 	return
				// }
				// if (parseFloat(this.minMoney) == parseFloat(this.lessMoney)) {
				// 	this.$notify({
				// 		title: '提示',
				// 		duration: 1800,
				// 		message: '优惠券金额不能高于最低消费金额',
				// 		type: 'warning'
				// 	});
				// 	return
				// }
				// if(this.couponType==2){
				if (this.goodsImages == '' || this.goodsImages == null) {
					this.$notify({
						title: '提示',
						duration: 1800,
						message: '请上传图片',
						type: 'warning'
					});
					return
				}
				// }
				if (this.couponType == 1) {
					// if (this.used_time === '' || this.used_time == null) {
					// 	this.$notify({
					// 		title: '提示',
					// 		duration: 1800,
					// 		message: '请输入使用时长',
					// 		type: 'warning'
					// 	});
					// 	return
					// }
					// if (this.minHour === '' ||this.minHour == null) {
					// 	this.$notify({
					// 		title: '提示',
					// 		duration: 1800,
					// 		message: '请输入最低使用时长',
					// 		type: 'warning'
					// 	});
					// 	return
					// }
					// if (parseFloat(this.used_time) <= parseFloat(this.minHour)) {
					// 	this.$notify({
					// 		title: '提示',
					// 		duration: 1800,
					// 		message: '使用时长不能小于最低使用时长',
					// 		type: 'warning'
					// 	});
					// 	return
					// }
				}
				
				this.$http({
					url: this.$http.adornUrl('selfCoupon/save'),
					method: 'post',
					data: this.$http.adornData({
						'couponName': this.couponName,
						'lessMoney': this.lessMoney,
						'minMoney': this.minMoney,
						'integral': this.integral,
						'sort': this.sort,
						'status': this.status,
						'type': this.type,
						'validDay': this.validDay,
						'hour': this.used_time,
						'goodsImages': this.goodsImages,
						'couponType': this.couponType,
						'minHour': this.minHour,
						'isLimitTime': this.isLimitTime,
						'weeks': this.weeks.toString(),
						'times': this.times.toString(),
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
			// 修改优惠券
			shopUpdates(index, row) {
				console.log(row);
				this.dialogFormVisible3 = true
				this.form.type = row.type
				this.form.couponId = row.couponId
				this.form.couponName = row.couponName
				this.form.lessMoney = row.lessMoney
				this.form.minMoney = row.minMoney
				this.form.integral = row.integral
				this.form.sort = row.sort
				this.form.status = row.status
				this.form.validDay = row.validDay

				this.form.createTime = row.createTime
				this.form.couponType = row.couponType
				



				this.form.goodsImages = row.goodsImages
				if (this.form.couponType == 1) {
					this.form.used_time = row.hour
					this.form.minHour = row.minHour
					if (row.minHour != '' && row.minHour) {
						this.minHourType = 1
					} else {
						this.minHourType = 2
					}

				} else {
					this.form.used_time = ''
					this.form.minHour = ''
				}

			},
			amendNoticeTo() {
				if (this.form.couponType == 2) {
					if (this.form.goodsImages == '' || this.form.goodsImages == null) {
						this.$notify({
							title: '提示',
							duration: 1800,
							message: '请上传图片',
							type: 'warning'
						});
						return
					}
				}
				if (this.form.couponType == 1) {
					// if (this.form.minHour === '' ||this.form.minHour == null) {
					// 	this.$notify({
					// 		title: '提示',
					// 		duration: 1800,
					// 		message: '请输入使用时长',
					// 		type: 'warning'
					// 	});
					// 	return
					// }
					// if (this.form.used_time === '' || this.form.used_time == null) {
					// 	this.$notify({
					// 		title: '提示',
					// 		duration: 1800,
					// 		message: '请输入使用时长',
					// 		type: 'warning'
					// 	});
					// 	return
					// }
					// if (this.form.minHour === '' ||this.form.minHour == null) {
					// 	this.$notify({
					// 		title: '提示',
					// 		duration: 1800,
					// 		message: '请输入最低使用时长',
					// 		type: 'warning'
					// 	});
					// 	return
					// }
					// if (parseFloat(this.form.used_time) <= parseFloat(this.form.minHour)) {
					// 	this.$notify({
					// 		title: '提示',
					// 		duration: 1800,
					// 		message: '使用时长不能小于最低使用时长',
					// 		type: 'warning'
					// 	});
					// 	return
					// }
				}
				
				this.$http({
					url: this.$http.adornUrl('selfCoupon/update'),
					method: 'post',
					data: this.$http.adornData({
						'couponId': this.form.couponId,
						'couponName': this.form.couponName,
						'hour': this.form.used_time,
						'lessMoney': this.form.lessMoney,
						'minMoney': this.form.minMoney,
						'sort': this.form.sort,
						'status': this.form.status,
						'type': this.form.type,
						'validDay': this.form.validDay,
						'integral': this.form.integral,
						'createTime': this.form.createTime,
						'goodsImages': this.form.goodsImages,
						'couponType': this.form.couponType,
						'minHour': this.form.minHour,
						


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
					this.dialogFormVisible3 = false
				})
			},
			// 发布优惠券弹框
			discuss(index, row) {
				console.log(row);
				this.couponType = row.couponType
				this.dialogFormVisible2 = true
				this.type = row.type
				this.goodsImages = row.goodsImages
				this.lessMoney = row.lessMoney
				this.validDay = row.validDay
				this.hour = row.hour
				this.minMoney = row.minMoney
				this.integral = row.integral
				this.sort = row.sort
				this.status = row.status
				this.couponId = row.couponId
				this.couponName1 = row.couponName
				this.store_id = ''
				this.teamRule = ''
				this.buyRule = ''
				this.issueNumber = ''
				this.isLimit = 1
				this.goods_ids = []
				this.goods_list = []
				this.typeyouhijuan = row.type
				this.minHour = row.minHour
				this.isLimitTime = 1
				this.weeks = []
				this.times = []
				this.startTime = ''
				this.endTime = ''
				this.shopSelect()
			},
			// 发布优惠券
			discussNoticeTo() {

				// if (this.store_id == '') {
				// 	this.$notify({
				// 		title: '提示',
				// 		duration: 1800,
				// 		message: '请选择充电站',
				// 		type: 'warning'
				// 	});
				// 	return
				// }
				// if (this.goods_ids.length == 0) {
				// 	this.$notify({
				// 		title: '提示',
				// 		duration: 1800,
				// 		message: '请选择充电桩',
				// 		type: 'warning'
				// 	});
				// 	return
				// }
				if (this.issueNumber === '') {
					this.$notify({
						title: '提示',
						duration: 1800,
						message: '请输入发布数量',
						type: 'warning'
					});
					return
				}
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
				var remainNumber = this.issueNumber
				if (this.teamRule == '') {
					this.$notify({
						title: '提示',
						duration: 1800,
						message: '请输入使用规则',
						type: 'warning'
					});
					return
				}
				if (this.buyRule == '') {
					this.$notify({
						title: '提示',
						duration: 1800,
						message: '请输入购买须知',
						type: 'warning'
					});
					return
				}

				this.$http({
					url: this.$http.adornUrl('selfCouponIssue/save'),
					method: 'post',
					data: this.$http.adornData({
						'couponId': this.couponId,
						'tearoomId': this.store_id,
						'goodsIds': this.goods_ids.toString(),
						'status': this.status,
						'type': this.typeyouhijuan,
						'teamRule': this.teamRule,
						'buyRule': this.buyRule,
						'issueNumber': this.issueNumber,
						'remainNumber': remainNumber,
						'hour': this.hour,
						'couponName': this.couponName1,
						'integral': this.integral,
						'goodsImages': this.goodsImages,
						'validDay': this.validDay,
						'isLimit': this.isLimit,
						'couponType': this.couponType,
						'minMoney': this.minMoney,
						'minHour': this.minHour,
						'isLimitTime': this.isLimitTime,
						'weeks': this.weeks.toString(),
						'times': this.times.toString(),

					})
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
						this.dialogFormVisible2 = false
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
			// 删除优惠券
			shopdeletes(row) {
				this.$confirm(`确定删除此条信息?`, '提示', {
					confirmButtonText: '确定',
					cancelButtonText: '取消',
					type: 'warning'
				}).then(() => {
					this.$http({
						url: this.$http.adornUrl('selfCoupon/delete'),
						method: 'get',
						params: this.$http.adornParams({
							'id': row.couponId
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
			//获取优惠券数据
			dataSelect() {
				let page = this.page - 1
				this.tableDataLoading = true
				this.$http({
					url: this.$http.adornUrl('selfCoupon/list'),
					method: 'get',
					params: this.$http.adornParams({
						'page': page,
						'size': this.size,
						'type': this.typeT,
						'status': this.statusT,
						'couponName': this.coupon_nameT
					})
				}).then(({
					data
				}) => {
					this.tableDataLoading = false
					let returnData = data.data;
					this.tableData = returnData
				})
			},
			//搜索
			select() {
				this.shopSelect()
			},

			// 删除
			clear(index) {
				this.imgs.splice(index, 1);
				this.ids.splice(index, 1)
			},
			// 选择充电站
			shopClick() {
				// for (var i in this.shopData) {

				this.$http({
					url: this.$http.adornUrl('goods/selectGoodsList'),
					method: 'get',
					params: this.$http.adornParams({
						'page': 1,
						'limit': 100,
						'goodsName': '',
						// 'goods_cate': this.typeId,
						'status': '',
						'tearoomId': this.store_id
					})
				}).then(({
					data
				}) => {
					let returnData = data.data;
					this.goods_list = returnData.list
				})
			},
			// 获取商品数据
			shopSelect() {

				this.tableDataLoading1 = true
				this.$http({
					url: this.$http.adornUrl('tearoom/selectTearoomList'),
					method: 'get',
					params: this.$http.adornParams({
						'page': 1,
						'limit': 1000,
						'tearoomName': this.storeNameT,
						// 'search_goods_cate': this.typeId,
						// 'status': status,
						'city': '',
						'status': '',
					})
				}).then(({
					data
				}) => {
					this.tableDataLoading1 = false
					let returnData = data.data.list;
					this.shopData = returnData
				})
			},
			// 选择商品
			selectClick(index, row) {
				this.dialogFormVisible1 = false
				this.goodsIds = row.id
				this.goodsImages = row.goods_image
				this.form.goodsIds = row.id
				this.form.goodsImages = row.goods_image

			},
			//上传成功
			handleAvatarSuccess(file, fileList) {
				console.log(file)
				this.goodsImages = file.data
			},
			handleAvatarSuccessF(file, fileList) {
				console.log(file)
				this.form.goodsImages = file.data
			},
			isLimitBtn() {
				if (this.isLimit == 2) {
					this.issueNumber = 0
				}
			},
			couponTypeBtn() {
				this.used_time = ''
				this.minHour = ''
				this.form.used_time = ''
				this.form.minHour = ''
			},
			minHourBtn() {
				if (this.minHourType == 1) {
					this.minHour = Number(this.used_time) + 1
					this.form.minHour = Number(this.form.used_time) + 1
				} else {
					this.minHour = ''
					this.form.minHour = ''
				}
			},
			// 添加时间
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
.coupon-page__filter-select {
	width: 150px;
}

.coupon-page__filter-input {
	width: 160px;
}

.coupon-page__filter-actions .el-button + .el-button {
	margin-left: 12px;
}

.coupon-page__tabs {
	margin-bottom: 0;
}

.coupon-page__tabs >>> .el-tabs__header {
	margin-bottom: 0;
}

.coupon-page__tabs .nf-table-card {
	margin-bottom: 0;
}

.coupon-page__tabs >>> .el-tabs__item.is-active {
	color: #5cc477;
}

.coupon-page__tabs >>> .el-tabs__active-bar {
	background-color: #5cc477;
}

.coupon-page__status-on {
	color: #5cc477;
}

.coupon-page__pagination {
	display: flex;
	justify-content: center;
	padding-top: 12px;
}
</style>

<style>
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