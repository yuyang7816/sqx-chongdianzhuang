<template>
	<div class="nf-page locality-page">
		<div class="nf-page-hero">
			<span class="nf-page-hero__badge">活动管理</span>
			<h2 class="nf-page-hero__title">活动管理</h2>
			<p class="nf-page-hero__desc">管理本地活动发布、报名与审核，支持按状态、类型、店铺、城市及时间筛选，可进行活动生命周期操作。</p>
			<div class="nf-page-hero__bar"></div>
		</div>

		<div class="nf-filter-card">
			<div class="nf-filter-card__head">
				<i class="el-icon-search nf-filter-card__head-icon"></i>
				<span>筛选条件</span>
			</div>
			<el-form :inline="true" class="nf-filter-card__body" @submit.native.prevent>
				<el-form-item label="状态">
					<el-select class="locality-page__filter-select" v-model="statusT" placeholder="请选择状态"
						@change="phoneSelect(status)">
						<el-option v-for="item in statuss" :key="item.value" :label="item.title" :value="item.id">
						</el-option>
					</el-select>
				</el-form-item>
				<el-form-item label="活动类型">
					<el-select class="locality-page__filter-select" v-model="gameIdT" placeholder="请选择活动类型"
						@change="phoneSelect()">
						<el-option v-for="item in homeData1" :key="item.id" :label="item.gameName" :value="item.id">
						</el-option>
					</el-select>
				</el-form-item>
				<el-form-item label="活动名称">
					<el-input class="locality-page__filter-input" @keydown.enter.native="phoneSelect"
						placeholder="请输入活动名称" v-model="activityNameT">
					</el-input>
				</el-form-item>
				<el-form-item label="店铺名称">
					<el-input class="locality-page__filter-input" @keydown.enter.native="phoneSelect"
						placeholder="请输入店铺名称" v-model="shopNameT">
					</el-input>
				</el-form-item>
				<el-form-item label="城市">
					<el-input class="locality-page__filter-input" @keydown.enter.native="phoneSelect"
						placeholder="请输入城市" v-model="cityT">
					</el-input>
				</el-form-item>
				<el-form-item label="联系电话">
					<el-input class="locality-page__filter-input" @keydown.enter.native="phoneSelect"
						placeholder="请输入联系电话" v-model="phoneT">
					</el-input>
				</el-form-item>
				<el-form-item label="发布人昵称">
					<el-input class="locality-page__filter-input" @keydown.enter.native="phoneSelect"
						placeholder="请输入发布人昵称" v-model="userNameT">
					</el-input>
				</el-form-item>
				<el-form-item label="时间类型">
					<el-select class="locality-page__filter-select" v-model="flag" placeholder="请选择时间类型"
						@change="animeDat3">
						<el-option v-for="item in flags" :key="item.value" :label="item.label" :value="item.value">
						</el-option>
					</el-select>
				</el-form-item>
				<el-form-item label="日期">
					<el-date-picker class="locality-page__filter-date" v-model="info1.stockDate1" align="right"
						type="date" format="yyyy-MM-dd" value-format="yyyy-MM-dd" placeholder="选择日期"
						@change="animeSelect3">
					</el-date-picker>
				</el-form-item>
				<el-form-item class="locality-page__filter-actions">
					<el-button type="primary" icon="el-icon-search" @click="phoneSelect">查询</el-button>
					<el-button type="primary" icon="el-icon-refresh-left" @click="cleans2">重置</el-button>
					<el-button type="primary" :disabled="!isAuth('locality:add')" icon="el-icon-plus"
						@click="choiaddNotice()">添加活动</el-button>
				</el-form-item>
			</el-form>
		</div>

		<div class="nf-table-card">
		<el-table v-loading="tableDataLoading" :data="choicenData.list">
			<el-table-column prop="activityId" label="编号" width="80" fixed="left">
			</el-table-column>
			<el-table-column prop="nickName" label="发布人昵称" fixed="left">
				<template slot-scope="scope">
					　<span class="locality-page__link"
						@click="updatesvideo(scope.row.userId)">{{scope.row.userName?scope.row.userName:'未绑定'}}</span>
				</template>
			</el-table-column>
			<el-table-column prop="activityPhone" label="联系电话" width="100">
			</el-table-column>
			<el-table-column prop="gameName" label="活动类型">
			</el-table-column>

			<el-table-column prop="shopName" label="店铺名称" width="150">
			</el-table-column>
			<el-table-column prop="scShop.shopAddress" label="商家地址" width="150">
				<template slot-scope="scope">
					<div >
						{{scope.row.activityProvince}}{{scope.row.activityCity}}{{scope.row.activityDistrict}}{{scope.row.activityAddress}}
					</div>
					<div>经度：{{scope.row.activityLongitude ? scope.row.activityLongitude : '未绑定'}}</div>
					<div>纬度：{{scope.row.activityLatitude ? scope.row.activityLatitude : '未绑定'}}</div>
				</template>
			</el-table-column>
			<el-table-column prop="activityName" label="活动名称" width="150">
			</el-table-column>
			<el-table-column prop="activityImg" label="主图" width="200">
				<template slot-scope="scope">
					<div v-if="scope.row.activityImg">
						<div v-for="(item,index) in scope.row.activityImg.split(',')" :key="index"
							style="display: inline-block; margin: 3px;">
							<el-popover placement="top-start" title="" trigger="hover">
								<img style="width: 50px; height: 50px" :src="item" alt="" slot="reference">
								<img style="width: 300px; height: auto" :src="item" alt="">
							</el-popover>
						</div>
					</div>

				</template>
			</el-table-column>
			<el-table-column prop="activityDetailsImg" label="展示图" width="200">
				<template slot-scope="scope">
					<div v-if="scope.row.activityDetailsImg">
						<div v-for="(item,index) in scope.row.activityDetailsImg.split(',')" :key="index"
							style="display: inline-block; margin: 3px;">
							<el-popover placement="top-start" title="" trigger="hover">
								<img style="width: 50px; height: 50px" :src="item" alt="" slot="reference">
								<img style="width: 300px; height: auto" :src="item" alt="">
							</el-popover>
						</div>
					</div>
				</template>
			</el-table-column>
			<el-table-column prop="activityContent" label="活动规则" width="250">
				<template slot-scope="scope">
					<el-popover placement="top-start" title="" trigger="hover">
						<div style="display: -webkit-box;-webkit-box-orient: vertical;-webkit-line-clamp: 3;overflow: hidden;max-height:80px;"
							slot="reference">{{scope.row.activityContent}}</div>
						<div style="width: 300px;">{{scope.row.activityContent}}</div>
					</el-popover>
				</template>
			</el-table-column>
			<!-- <el-table-column prop="activityDetails" label="活动须知" width="250">
				<template slot-scope="scope">
					<el-popover placement="top-start" title="" trigger="hover">
						<div style="display: -webkit-box;-webkit-box-orient: vertical;-webkit-line-clamp: 3;overflow: hidden;max-height:80px;"
							slot="reference">{{scope.row.activityDetails}}</div>
						<div style="width: 300px;">{{scope.row.activityDetails}}</div>
					</el-popover>
				</template>
			</el-table-column> -->
			<el-table-column prop="activityDate" label="活动开始时间" width="100">
			</el-table-column>
			<el-table-column prop="activityEndTime" label="活动结束时间" width="100">
				<template slot-scope="scope">
					<span>{{scope.row.activityEndTime?scope.row.activityEndTime:'未设置'}}</span>
				</template>
			</el-table-column>
			<el-table-column prop="counts" label="已报名人数">
			</el-table-column>
			<el-table-column prop="signCounts" label="已签到人数">
			</el-table-column>
			<el-table-column prop="vipIndex" label="报名会员等级">
				<template slot-scope="scope">
					<div v-if="scope.row.vipIndex==0">不限等级</div>
					<div v-if="scope.row.vipIndex==null">非会员用户</div>
					<div v-if="scope.row.vipIndex==1">普通会员</div>
					<div v-if="scope.row.vipIndex==2">银卡会员</div>
					<div v-if="scope.row.vipIndex==3">金卡会员</div>
					<div v-if="scope.row.vipIndex==4">黑卡会员</div>
					
				</template>
			</el-table-column>
			<el-table-column prop="isSex" label="是否限制男女">
				<template slot-scope="scope">
					<div v-if="scope.row.isSex==1">是</div>
					<div v-else>否</div>
				</template>
			</el-table-column>
			<el-table-column prop="giftMouseNum" label="活动人数" width="160">
				<template slot-scope="scope">

					<div v-if="scope.row.isSex==1">
						<div>男生人数：{{scope.row.activityMan}}</div>
						<div>女生人数：{{scope.row.activityGirl}}</div>
					</div>
					<div v-else>
						<div>总人数：{{scope.row.activityGirl}}</div>
					</div>
				</template>
			</el-table-column>

			<el-table-column prop="activityEndDate" label="报名截止时间" width="160">
			</el-table-column>
			<!-- <el-table-column prop="priceType" label="付费类型">
				<template slot-scope="scope">
					<div v-if="scope.row.priceType==1">指定</div>
					<div v-if="scope.row.priceType==2">默认</div>
				</template>
			</el-table-column> -->
			<el-table-column prop="paymentType" label="费用方式">
				<template slot-scope="scope">
					<div v-if="scope.row.paymentType==1">线上收费</div>
					<div v-if="scope.row.paymentType==2">免费</div>
					<div v-if="scope.row.paymentType==3">线下收费</div>
					<div v-if="scope.row.paymentType==4">我买单</div>
					
				</template>
			</el-table-column>
			<el-table-column prop="priceType" label="活动费用">
				<template slot-scope="scope">
					<div v-if="scope.row.priceType==1">
						<div>男生活动费用：{{scope.row.price}}</div>
						<div>女生活动费用：{{scope.row.girlPrice}}</div>
					</div>
					<div v-else>{{scope.row.price}}</div>
				</template>
			</el-table-column>
			<el-table-column prop="auditContent" label="审核理由" width="160">
			</el-table-column>
			<el-table-column prop="status" label="状态" width="100" fixed='right'>
				<template slot-scope="scope">
					<span style="color: #5cc477;" v-if="scope.row.status === 1 ">报名中</span>
					<span style="color: #13ce66;" v-if="scope.row.status === 2 ">进行中</span>
					<span style="color: #ff4949;" v-if="scope.row.status === 3 ">已结束</span>
					<span style="color: #999;" v-if="scope.row.status === 5 ">已取消</span>
					<span style="color: orange;" v-if="scope.row.status === 6 ">待开始</span>
					<span style="color: #ff4949;" v-if="scope.row.status === 7 ">待审核</span>
					<span style="color: #999;" v-if="scope.row.status === 8 ">已拒绝</span>

				</template>
			</el-table-column>
			<el-table-column prop="createTime" label="创建时间" width="160">
			</el-table-column>
			<el-table-column label="操作" prop="id" width="200" fixed='right'>
				<template slot-scope="scope">
					<el-button size="mini" type="primary" style="margin: 3px;" plain
						@click="shenhe( scope.row)">报名列表</el-button>
					<el-button size="mini" type="primary" style="margin: 3px;" @click="choiCompile(scope.row)"
						v-if="scope.row.status != 3 ">修改
					</el-button>
					<el-button size="mini" type="warning" style="margin: 3px;" plain
						@click="qiandao( scope.row)">签到列表</el-button>
					<el-button size="mini" type="danger" :disabled="!isAuth('locality:delete')" style="margin: 3px;"
						@click="choidelete(scope.row)">删除
					</el-button>
					<el-button size="mini" type="primary" v-if="scope.row.status === 6 "
						:disabled="!isAuth('locality:update')" style="margin: 3px;" @click="jieshuBtn(scope.row)">开始活动
					</el-button>
					<el-button size="mini" type="info"
						v-if="scope.row.status === 1||scope.row.status === 6||scope.row.status === 7||scope.row.status === 8 "
						:disabled="!isAuth('locality:update')" style="margin: 3px;" @click="quxiaoBtn(scope.row)">取消活动
					</el-button>
					<el-button size="mini" type="primary" v-if="scope.row.status === 1 "
						:disabled="!isAuth('locality:update')" style="margin: 3px;" @click="tingzhi(scope.row)">停止报名
					</el-button>
					<el-button size="mini" type="primary" v-if="scope.row.status === 2 "
						:disabled="!isAuth('locality:update')" style="margin: 3px;" @click="jieshu(scope.row)">结束活动
					</el-button>
					<el-button size="mini" type="danger" v-if="scope.row.status === 7 "
						:disabled="!isAuth('locality:shenhe')" style="margin: 3px;" @click="shenhedelete(scope.row)">审核
					</el-button>
					<el-button size="mini" type="danger" plain
						:disabled="!isAuth('locality:tuisong')" style="margin: 3px;" @click="tuisongBtn(scope.row)">推送消息
					</el-button>
				</template>
			</el-table-column>
		</el-table>
		<div class="locality-page__stats">
			<div class="locality-page__stat-item">
				<span class="locality-page__stat-label">本页累计活动报名费</span>
				<span class="locality-page__stat-value">{{totalMoney.toFixed(2)}}</span>
				<span class="locality-page__stat-unit">元</span>
			</div>
			<div class="locality-page__stat-item">
				<span class="locality-page__stat-label">累计活动报名人数</span>
				<span class="locality-page__stat-value">{{totalBaoming}}</span>
				<span class="locality-page__stat-unit">人</span>
			</div>
			<div class="locality-page__stat-item">
				<span class="locality-page__stat-label">累计活动签到人数</span>
				<span class="locality-page__stat-value">{{totalQiandao}}</span>
				<span class="locality-page__stat-unit">人</span>
			</div>
		</div>
		<div class="locality-page__pagination">
			<el-pagination @size-change="handleSizeChange1" @current-change="handleCurrentChange1"
				:page-sizes="[5,10, 20, 30, 50, 100]" :page-size="size" :current-page="page"
				layout="total,sizes, prev, pager, next,jumper" :total="choicenData.totalCount">
			</el-pagination>
		</div>
		</div>

		<!-- 添加、修改活动 弹框 -->
		<el-dialog :title="renwuTitles" customClass="customWidth" :visible.sync="dialogFormVisible5" center>
			<!-- <div style="margin-bottom: 10px;">
				<span style="width: 200px;display: inline-block;text-align: right;">活动分类：</span>
				<el-radio-group v-model="activityType" @change="activityTypeBtn()">
					<el-radio :label="1" v-if="isFabuGame==1">猫鼠游戏</el-radio>
					<el-radio :label="2">其他活动</el-radio>
				</el-radio-group>
			</div> -->
			<div style="margin-bottom: 10px;">
				<span style="width: 200px;display: inline-block;text-align: right;">活动类型：</span>
				<el-select v-model="gameId" style="width:50%;" @change="gameBtn()">
					<el-option v-for="(item,index) in homeData" :key="index" :label="item.gameName" :value="item.id">
					</el-option>
				</el-select>
			</div>
			<div style="margin-bottom: 10px;">
				<span style="width: 200px;display: inline-block;text-align: right;">商家：</span>
				<el-input v-model="shopName" @focus="shopselect" style="width:50%;" placeholder="请选择商家">
				</el-input>
			</div>
			<!-- <div style="margin-bottom: 10px;">
				<span style="width: 200px;display: inline-block;text-align: right;">发布人：</span>
				<el-input v-model="nickName1" @focus="userselect" style="width:50%;" placeholder="请选择发布人">
				</el-input>
			</div> -->
			<div style="margin-bottom: 10px;">
				<span style="width: 200px;display: inline-block;text-align: right;">活动名称：</span>
				<el-input v-model="activityName" style="width:50%;" placeholder="请输入活动名称"></el-input>
			</div>
			<div style="margin-bottom: 10px;">
				<span style="width: 200px;display: inline-block;text-align: right;">是否限制男女：</span>
				<el-radio-group v-model="isSex">
					<el-radio :label="1">男</el-radio>
					<el-radio :label="2">女</el-radio>
					<el-radio :label="3">不限制</el-radio>
				</el-radio-group>
			</div>
			<div style="margin-bottom: 10px;" v-if="isSex==1">
				<span style="width: 200px;display: inline-block;text-align: right;">男生人数：</span>
				<el-input v-model="activityMan" style="width:50%;" placeholder="请输入男生人数"></el-input>
			</div>
			<div style="margin-bottom: 10px;" v-if="isSex==2">
				<span style="width: 200px;display: inline-block;text-align: right;">女生人数：</span>
				<el-input v-model="activityGirl" style="width:50%;" placeholder="请输入女生人数"></el-input>
			</div>
			<div style="margin-bottom: 10px;" v-if="isSex==3">
				<span style="width: 200px;display: inline-block;text-align: right;">总人数：</span>
				<el-input v-model="activityGirl" style="width:50%;" placeholder="请输入总人数"></el-input>
			</div>
			
			<div style="margin-bottom: 10px;">
				<span style="width: 200px;display: inline-block;text-align: right;">报名会员等级：</span>
				<el-radio-group v-model="vipIndex">
					<el-radio v-for="(item,index) in tableDataVip" :key="index" :label="item.vipIndex">{{item.vipName}}</el-radio>
					<!-- <el-radio :label="2">默认</el-radio> -->
				</el-radio-group>
			</div>
			<div style="margin-bottom: 10px;">
				<span style="width: 200px;display: inline-block;text-align: right;">活动开始时间：</span>
				<el-date-picker style="width: 50%;" v-model="activityDate" align="right" type="datetime"
					format="yyyy-MM-dd HH:mm:ss" value-format="yyyy-MM-dd HH:mm:ss" placeholder="选择活动开始时间">
				</el-date-picker>
			</div>
			<div style="margin-bottom: 10px;">
				<span style="width: 200px;display: inline-block;text-align: right;">报名截止时间：</span>
				<span class="block">
					<el-date-picker style="width: 50%;" v-model="activityEndDate" align="right" type="datetime"
						format="yyyy-MM-dd HH:mm:ss" value-format="yyyy-MM-dd HH:mm:ss" placeholder="选择报名截止时间">
					</el-date-picker>
				</span>
			</div>
			<!-- <div style="margin-bottom: 10px;display:flex;">
					<span style="width: 200px;display: inline-block;text-align: right;">主页图：</span>
					<div
						style=" width:148px;height:148px;border: 1px dashed #c0ccda;border-radius: 6px;text-align: center;line-height: 148px;">
						<el-upload class="avatar-uploader" v-model="homepageImg"
							:action="$http.adornUrlUp('alioss/upload')" :show-file-list="false"
							:on-success="handleAvatarSuccess5" :on-progress="onprogress">
							<img v-if="homepageImg!=''&&percentage==100" :src="homepageImg" class="avatar"
								style="border-radius: 6px;width: 148px;height: 148px;" />
							<i v-else class="el-icon-plus avatar-uploader-icon iconss"></i>
							<el-progress v-if="percentage>0&&percentage<100" type="circle" :percentage="percentage">
							</el-progress>
						</el-upload>
					</div>
				</div> -->
			<div style="margin-bottom: 10px;display: flex;align-items: center;">
				<span style="width: 200px;display: inline-block;text-align: right;">主图：</span>
				<div style="display: flex;flex-wrap: wrap;">
					<div class="imgs" v-for="(item,index) in activityImg" :key="index">
						<img width="100%" class="images" height="100%" :src="item" alt="">
						<span class="dels">
							<i class="el-icon-delete" @click="dels(index)"></i>
						</span>
					</div>
					<div class="imgs">
						<el-upload :action="$http.adornUrlUp('alioss/upload')"
							style="border: 1px dashed #c0ccda;text-align: center;line-height: 148px; "
							:show-file-list="false" :on-success="handleRemove" v-if="activityImg.length<1">
							<i class="el-icon-plus"></i>
						</el-upload>
						<el-dialog :visible.sync="dialogVisible">
							<img width="100%" :src="dialogImageUrl" alt="">
						</el-dialog>
					</div>
				</div>

			</div>
			<div style="margin-bottom: 10px;display: flex;align-items: center;">
				<span style="width: 200px;display: inline-block;text-align: right;">活动展示图：</span>
				<div style="display: flex;flex-wrap: wrap;">
					<div class="imgs" v-for="(item,index) in activityDetailsImg" :key="index">
						<img width="100%" class="images" height="100%" :src="item" alt="">
						<span class="dels">
							<i class="el-icon-delete" @click="dels2(index)"></i>
						</span>
					</div>
					<div class="imgs">
						<el-upload :action="$http.adornUrlUp('alioss/upload')"
							style="border: 1px dashed #c0ccda;text-align: center;line-height: 148px; "
							:show-file-list="false" :on-success="handleRemove2" v-if="activityDetailsImg.length<5">
							<i class="el-icon-plus"></i>
						</el-upload>
						<el-dialog :visible.sync="dialogVisible">
							<img width="100%" :src="dialogImageUrl" alt="">
						</el-dialog>
					</div>
				</div>

			</div>
			<div style="margin-bottom: 10px;">
				<span style="width: 200px;display: inline-block;text-align: right;">联系人电话：</span>
				<el-input v-model="activityPhone" type="number" style="width:50%;" min="0"
					placeholder="请输入联系人电话"></el-input>
			</div>
			<!-- 
			<div style="margin-bottom: 10px;">
				<span style="width: 200px;display: inline-block;text-align: right;">付费类型：</span>
				<el-radio-group v-model="priceType">
					<el-radio :label="1">指定</el-radio>
					<el-radio :label="2">默认</el-radio>
				</el-radio-group>
			</div> -->
			<div style="margin-bottom: 10px;">
				<span style="width: 200px;display: inline-block;text-align: right;">费用方式：</span>
				<el-radio-group v-model="paymentType" @change="paymentTypeNtm()">
					<el-radio :label="1">线上收费</el-radio>
					<el-radio :label="2">免费</el-radio>
					<el-radio :label="3">线下收费</el-radio>
					<el-radio :label="4">我买单</el-radio>
				</el-radio-group>
			</div>
			<!-- <div style="margin-bottom: 10px;" v-if="priceType==1&&paymentType!=2">
				<span style="width: 200px;display: inline-block;text-align: right;">男生活动费用：</span>
				<el-input v-model="price" type="number" style="width:50%;" min="0" placeholder="请输入男生活动费用"></el-input>
			</div>
			<div style="margin-bottom: 10px;" v-if="priceType==1&&paymentType!=2">
				<span style="width: 200px;display: inline-block;text-align: right;">女生活动费用：</span>
				<el-input v-model="girlPrice" type="number" style="width:50%;" min="0" placeholder="请输入女生活动费用"></el-input>
			</div> -->
			<div style="margin-bottom: 10px;" v-if="paymentType!=2&&paymentType!=4">
				<span style="width: 200px;display: inline-block;text-align: right;">活动费用：</span>
				<el-input v-model="price" type="number" style="width:50%;" min="0" placeholder="请输入活动费用"></el-input>
			</div>
			<div style="margin-bottom: 10px;">
				<span style="width: 200px;display: inline-block;text-align: right;">活动规则：</span>
				<el-input v-model="activityContent" style="width:50%;" placeholder="请输入活动规则" type="textarea"
					:rows="3"></el-input>
			</div>
			<!-- <div style="margin-bottom: 10px;">
				<span style="width: 200px;display: inline-block;text-align: right;">活动须知：</span>
				<el-input v-model="activityDetails" style="width:50%;" placeholder="请输入活动须知" type="textarea"
					:rows="3"></el-input>
			</div> -->
			<!-- <div style="margin-bottom: 10px;" v-if="activityType==1">
				<span style="width: 200px;display: inline-block;text-align: right;">活动区域：</span>
				<el-select v-model="areaId" style="width:50%;" filterable @change="areaBtn()">
					<el-option v-for="(item,index) in areaData" :key="index" :label="item.addressName"
						:value="item.areaId">
					</el-option>
				</el-select>
			</div> -->

			<div slot="footer" class="dialog-footer" style="margin-top: 30px;text-align: center;">
				<el-button @click="dialogFormVisible5=false">取 消</el-button>
				<el-button type="primary" @click="addmissionNoticeTo()">确 定</el-button>
			</div>
		</el-dialog>
		
		<!-- 用户列表 -->
		<el-dialog title="用户列表" :visible.sync="dialogFormVisible3" center width="70%">
			<div style="margin:5px;display: inline-block;">
				<el-input style="width: 150px;" @keydown.enter.native="userclick" clearable placeholder="请输入手机号"
					v-model="phone1"></el-input>&nbsp;&nbsp;&nbsp;&nbsp;
				<el-input style="width: 150px;" @keydown.enter.native="userclick" clearable placeholder="请输入昵称"
					v-model="nickName2"></el-input>
				<el-button style='margin-left:15px;' size="mini" type="primary" icon="document" @click="userclick">
					查询
				</el-button>
				<el-button style='margin-left:15px;' size="mini" type="primary" icon="document" @click="userclose">
					重置
				</el-button>
			</div>
			<el-table width="780px" v-loading="tableDataLoading4" :data="userIds.list">
				<el-table-column prop="userId" width="80" label="编号">
				</el-table-column>
				<el-table-column prop="nickName" label="昵称">
					<template slot-scope="scope">
						<span style="color: #f56c6c;">{{scope.row.userName ? scope.row.userName : '未设置'}}</span>
					</template>
				</el-table-column>
				<el-table-column prop="phone" label="手机号">
					<template slot-scope="scope">
						<span>{{scope.row.phone ? scope.row.phone : '未绑定'}}</span>
					</template>
				</el-table-column>
				<el-table-column prop="createTime" label="创建时间">
				</el-table-column>
				<el-table-column label="操作" fixed='right' width="120">
					<template slot-scope="scope">
						<el-button size="mini" type="primary" @click="confirm(scope.row)">确定
						</el-button>
					</template>
				</el-table-column>
			</el-table>
			<div style="text-align: center;margin-top: 10px;">
				<el-pagination @size-change="handleSizeChange2" @current-change="handleCurrentChange2"
					:page-sizes="[10, 20, 30, 50, 100]" :page-size="size1" :current-page="page1"
					layout="total,sizes, prev, pager, next,jumper" :total="userIds.totalCount">
				</el-pagination>
			</div>
		</el-dialog>

		<!-- 报名列表 -->
		<el-dialog title="报名列表" :visible.sync="dialogFormVisible8" center width="80%">
			<el-table width="780px" v-loading="tableDataLoadingB" :data="baomingData.list">
				<el-table-column label="编号" prop="activityApplyId" width="80">
				</el-table-column>
				<el-table-column prop="nickName" label="昵称">
					<template slot-scope="scope">
						<span
							v-if="scope.row.userEntity">{{scope.row.userEntity? scope.row.userEntity.userName : '未绑定'}}</span>
					</template>
				</el-table-column>
				<el-table-column prop="phone" label="手机号">
					<template slot-scope="scope">
						<span
							v-if="scope.row.userEntity">{{scope.row.userEntity? scope.row.userEntity.phone : '未绑定'}}</span>
					</template>
				</el-table-column>
				<el-table-column prop="phone" label="性别">
					<template slot-scope="scope">
						<span>{{scope.row.sex==1 ? '男' : '女'}}</span>
					</template>
				</el-table-column>
				<el-table-column prop="ordersNo" label="订单号">
				</el-table-column>

				<el-table-column prop="payMoney" label="支付金额">
					<template slot-scope="scope">
						　<span style="color: red;cursor: pointer;"
							@click="updatesvideo(scope.row.payMoney)">{{scope.row.payMoney?scope.row.payMoney:'0'}}</span>
					</template>
				</el-table-column>

				<el-table-column prop="nickName" label="一级佣金">
					<template slot-scope="scope">
						　<span style="color: red;cursor: pointer;"
							@click="updatesvideo(scope.row.zhiUserId)">{{scope.row.zhiMoney?scope.row.zhiMoney:'0'}}</span>
					</template>
				</el-table-column>
				<el-table-column prop="zhiUserName" label="一级用户昵称">
					<template slot-scope="scope">
						　<span style="color: #13ce66;cursor: pointer;"
							@click="updatesvideo(scope.row.zhiUserId)">{{scope.row.zhiUserName?scope.row.zhiUserName:'-'}}</span>
					</template>
				</el-table-column>
				<el-table-column prop="nickName" label="二级佣金">
					<template slot-scope="scope">
						　<span style="color: red;cursor: pointer;"
							@click="updatesvideo(scope.row.feiUserId)">{{scope.row.feiMoney?scope.row.feiMoney:'0'}}</span>
					</template>
				</el-table-column>
				<el-table-column prop="feiUserName" label="二级用户昵称">
					<template slot-scope="scope">
						　<span style="color: #13ce66;cursor: pointer;"
							@click="updatesvideo(scope.row.zhiUserId)">{{scope.row.feiUserName?scope.row.feiUserName:'-'}}</span>
					</template>
				</el-table-column>
				<el-table-column prop="pingMoney" label="平台佣金">
					<template slot-scope="scope">
						　<span
							style="color: red;cursor: pointer;">{{scope.row.pingMoney?scope.row.pingMoney:'0'}}</span>
					</template>
				</el-table-column>
				<el-table-column prop="createTime" label="创建时间">
				</el-table-column>
				<el-table-column label="状态" width="120">
					<template slot-scope="scope">
						<span v-if="scope.row.status==1" style="color: #f56c6c;">已报名</span>
						<span v-if="scope.row.status==2" style="color: #5cc477;">进行中</span>
						<span v-if="scope.row.status==3" style="color: #f56c6c;">已结束</span>
						<span v-if="scope.row.status==4" style="color: chocolate;">已签到</span>
						<span v-if="scope.row.status==5" style="color: #999;">已取消</span>
					</template>
				</el-table-column>
			</el-table>
			<div style="text-align: center;margin-top: 10px;">
				<el-pagination @size-change="handleSizeChangeBm" @current-change="handleCurrentChangeBm"
					:page-sizes="[10, 20, 30, 50, 100]" :page-size="limitB" :current-page="pageB"
					layout="total,sizes, prev, pager, next,jumper" :total="baomingData.totalCount">
				</el-pagination>
			</div>
		</el-dialog>

		<!-- 签到列表 -->
		<el-dialog title="签到列表" :visible.sync="dialogFormVisibleQd" center width="80%">
			<el-table v-loading="tableDataLoadingB" :data="baomingData.records">
				<el-table-column prop="id" label="编号" width="80">
				</el-table-column>
				<el-table-column prop="userName" label="签到用户">
					<template slot-scope="scope">
						　<span v-if="scope.row.userName" style="color: #5cc477;cursor: pointer;"
							@click="updatesvideo(scope.row.userId)">
							{{scope.row.userName? scope.row.userName : '未绑定'}}
						</span>
					</template>
				</el-table-column>
				<el-table-column prop="phone" label="手机号">
				</el-table-column>
				<el-table-column prop="title" label="活动名称">
				</el-table-column>
				<el-table-column prop="createAt" label="签到时间">
				</el-table-column>
			</el-table>
			<div style="text-align: center;margin-top: 10px;">
				<el-pagination @size-change="handleSizeChangeQd" @current-change="handleCurrentChangeQd"
					:page-sizes="[10, 20, 30, 50, 100]" :page-size="limitB" :current-page="pageB"
					layout="total,sizes, prev, pager, next,jumper" :total="baomingData.total">
				</el-pagination>
			</div>
		</el-dialog>
		<!-- 活动相册  -->
		<el-dialog title="活动相册" :visible.sync="dialogFormVisibleXc" center width="80%">
			<el-table v-loading="tableDataLoadingB" :data="baomingData.list">
				<el-table-column prop="activityPhotoId" label="编号" width="80">
				</el-table-column>
				<el-table-column prop="userName" label="上传用户" width="120">
					<template slot-scope="scope">
						　<span v-if="scope.row.userName" style="color: #5cc477;cursor: pointer;"
							@click="updatesvideo(scope.row.userId)">
							{{scope.row.userName? scope.row.userName : '未绑定'}}
						</span>
					</template>
				</el-table-column>
				<el-table-column prop="phone" label="手机号" width="120">
				</el-table-column>
				<el-table-column prop="photo" label="上传图片">
					<template slot-scope="scope">
						<div v-if="scope.row.photo">
							<div v-for="(item,index) in scope.row.photo.split(',')" :key="index"
								style="display: inline-block; margin: 3px;">
								<el-popover placement="top-start" title="" trigger="hover">
									<img style="width: 50px; height: 50px" :src="item" alt="" slot="reference">
									<img style="width: 300px; height: auto" :src="item" alt="">
								</el-popover>
							</div>
						</div>
					</template>
				</el-table-column>
				<el-table-column prop="createTime" label="上传时间" width="160">
				</el-table-column>
				<el-table-column prop="photo" label="操作" width="150">
					<template slot-scope="scope">
						<el-button size="mini" type="danger" :disabled="!isAuth('locality:delete')" style="margin: 3px;"
							@click="choideleteDe(scope.row)">删除
						</el-button>
					</template>
				</el-table-column>

			</el-table>
			<div style="text-align: center;margin-top: 10px;">
				<el-pagination @size-change="handleSizeChangeXc" @current-change="handleCurrentChangeXc"
					:page-sizes="[10, 20, 30, 50, 100]" :page-size="limitB" :current-page="pageB"
					layout="total,sizes, prev, pager, next,jumper" :total="baomingData.totalCount">
				</el-pagination>
			</div>
		</el-dialog>
		<!-- 商家列表  -->
		<el-dialog title="商家列表" :visible.sync="dialogFormVisibleBh" center width="80%">
			<div style="position: relative;display: inline-block;margin:5px;">
				<span>店铺名称:</span>
				<el-input style="width: 200px;" @keydown.enter.native="buhuoBtns" placeholder="请输入店铺名称"
					v-model="shopNameB">
				</el-input>&nbsp;&nbsp;
			</div>
			<div style="position: relative;display: inline-block;margin:5px;">
				<span>手机号:</span>
				<el-input style="width: 200px;" @keydown.enter.native="buhuoBtns" placeholder="请输入手机号" v-model="phoneB">
				</el-input>&nbsp;&nbsp;
			</div>
			<el-button size="mini" type="primary" style="margin: 3px;" @click="buhuoBtns()">查询
			</el-button>
			<el-table v-loading="tableDataLoadingB" :data="baomingData.list">
				<el-table-column fixed="left" prop="tearoomId" label="编号" width="80">
				</el-table-column>
				<el-table-column prop="tearoomName" fixed="left" label="台球馆名称" width="120">
				</el-table-column>
				<el-table-column prop="tearoomTitleImage" label="台球馆logo">
					<template slot-scope="scope">
						<span v-if="scope.row.tearoomTitleImage == null">暂无图片</span>
						<img v-else :src="scope.row.tearoomTitleImage" alt="" width="40" height="40">
					</template>
				</el-table-column>
				<el-table-column prop="nickname" label="用户昵称" width="120">
					<template slot-scope="scope">
						<span style="color: #5cc477;cursor: pointer;"
							@click="updatesvideo(scope.row.userId)">{{scope.row.userName ? scope.row.userName : '未绑定'}}</span>
					</template>
				</el-table-column>
				<el-table-column prop="store_address" label="台球馆地址" width="200">
					<template slot-scope="scope">
						<div>
							{{scope.row.tearoomProvince}}{{scope.row.tearoomCity}}{{scope.row.tearoomDistrict}}{{scope.row.tearoomAddress}}
						</div>
						<div>经度：{{scope.row.tearoomLongitude ? scope.row.tearoomLongitude : '未绑定'}}</div>
						<div>纬度：{{scope.row.tearoomLatitude ? scope.row.tearoomLatitude : '未绑定'}}</div>
					</template>
				</el-table-column>
				<el-table-column prop="startTime" label="营业时间" width="100">
					<template slot-scope="scope">
						<div>{{scope.row.startTime}}--{{scope.row.endTime}}</div>
					</template>
				</el-table-column>
				<el-table-column prop="sales" label="销量">
					<template slot-scope="scope">
						<span>{{scope.row.sales ? scope.row.sales : '0'}}</span>
					</template>
				</el-table-column>

				<el-table-column fixed="right" label="操作" width="160">
					<template slot-scope="scope">
						<el-button size="mini" type="primary" @click="xzShop(scope.row)"
							style="margin: 3px;">选择</el-button>

					</template>
				</el-table-column>
			</el-table>
			<div style="text-align: center;margin-top: 10px;">
				<el-pagination @size-change="handleSizeChangeBh" @current-change="handleCurrentChangeBh"
					:page-sizes="[10, 20, 30, 50, 100]" :page-size="limitB" :current-page="pageB"
					layout="total,sizes, prev, pager, next,jumper" :total="baomingData.totalCount">
				</el-pagination>
			</div>
		</el-dialog>
		<!-- 结束时间  -->
		<el-dialog title="结束时间" :visible.sync="dialogFormVisibleJssj" center width="80%">
			<div style="margin-bottom: 10px;">
				<span style="width: 200px;display: inline-block;text-align: right;">结束时间：</span>
				<span class="block">
					<el-date-picker style="width: 50%;" v-model="activityEndTime" align="right" type="datetime"
						format="yyyy-MM-dd HH:mm:ss" value-format="yyyy-MM-dd HH:mm:ss" placeholder="选择结束时间">
					</el-date-picker>
				</span>
			</div>
			<div slot="footer" class="dialog-footer" style="margin-top: 30px;text-align: center;">
				<el-button @click="dialogFormVisibleJssj=false">取 消</el-button>
				<el-button type="primary" @click="kaishi()">确 定</el-button>
			</div>
		</el-dialog>
		<!-- 审核  -->
		<el-dialog title="审核" :visible.sync="dialogFormVisibleSh" center width="80%">
			<div style="margin-bottom: 10px;">
				<span style="width: 200px;display: inline-block;text-align: right;">审核结果：</span>
				<el-radio-group v-model="statusSh">
					<el-radio :label="1">通过</el-radio>
					<el-radio :label="2">拒绝</el-radio>
				</el-radio-group>
			</div>
			<div style="margin-bottom: 10px;">
				<span style="width: 200px;display: inline-block;text-align: right;">审核理由：</span>
				<el-input v-model="auditContent" style="width:50%;" placeholder="请输入审核理由" type="textarea"
					:rows="3"></el-input>
			</div>
			<div slot="footer" class="dialog-footer" style="margin-top: 30px;text-align: center;">
				<el-button @click="dialogFormVisibleSh=false">取 消</el-button>
				<el-button type="primary" @click="shenheBtn()">确 定</el-button>
			</div>
		</el-dialog>
		<!-- 聊天内容 -->
		<el-dialog title="聊天内容" :visible.sync="dialogFormVisible" center width="80%">
			<div style="margin:2% 0;display: inline-block;">
				<span>内容:</span>
				<el-input style="width: 150px;" @keydown.enter.native="select1" clearable placeholder="请输入聊天内容"
					v-model="content">
				</el-input>
			</div>
			<div style="display: inline-block;">
				<el-button style='margin-left:15px;' size="mini" type="primary" icon="document"
					@click="select1">查询</el-button>
				<el-button style='margin-left:15px;' size="mini" type="primary" icon="document"
					@click="cleans1">重置</el-button>
			</div>
			<el-table v-loading="tableDataLoading" :data="userData.list">
				<el-table-column prop="activityChatContentId" label="编号" width="80">
				</el-table-column>
				<el-table-column prop="userName" label="用户昵称">
					<template slot-scope="scope">
						<div style="color: #5cc477;cursor: pointer;" @click="updates(scope.row.userId)">
							{{scope.row.userName ? scope.row.userName : '未绑定'}}</div>
					</template>
				</el-table-column>
				<el-table-column prop="title" label="用户头像">
					<template slot-scope="scope">
						<div v-if="scope.row.avatar == null || scope.row.avatar == ''">
							暂无图片
						</div>
						<div v-else>
							<el-popover placement="top-start" title="" trigger="hover">
								<img style="width: 50px; height: 50px" :src="scope.row.avatar" alt="" slot="reference">
								<img style="width: 200px; height: 200px" :src="scope.row.avatar" alt="">
							</el-popover>
						</div>
					</template>

				</el-table-column>
				<el-table-column prop="content" label="信息内容" width="280">
					<template slot-scope="scope">
						<div v-if="scope.row.messageType==1">{{scope.row.content}}</div>
						<div v-if="scope.row.messageType==2">
							<el-popover placement="top-start" title="" trigger="hover">
								<img style="width: 50px; height: 50px" :src="scope.row.content" alt="" slot="reference">
								<img style="width: 200px; height: 200px" :src="scope.row.content" alt="">
							</el-popover>
						</div>
						<div v-if="scope.row.messageType==3">
							<audio :src="scope.row.content" type="audio/wav" controls="controls"></audio>
						</div>
						<div v-if="scope.row.messageType==4">
							<el-popover placement="top-start" title="" trigger="hover">
								<img style="width: 50px; height: 50px" :src="$http.adornUrl('emoji/'+scope.row.content)"
									alt="" slot="reference">
								<img style="width: 200px; height: 200px"
									:src="$http.adornUrl('emoji/'+scope.row.content)" alt="">
							</el-popover>
						</div>

						<!-- <div  v-if="scope.row.messageType==4">
		          <el-popover placement="top-start" title="" trigger="hover">
		            <img style="width: 50px; height: 50px" :src="$http.adornUrl('emoji/'+scope.row.content)" alt="" slot="reference">
		            <img style="width: 200px; height: 200px" :src="$http.adornUrl('emoji/'+scope.row.content)" alt="">
		          </el-popover>
		        </div> -->

					</template>
				</el-table-column>
				<el-table-column prop="contentCount" label="用户是否删除" width="100">
					<template slot-scope="scope">
						<span style="color: #ff4949;" v-if="scope.row.isDelete==1 ">用户已删除</span>
						<span v-else>否</span>
					</template>
				</el-table-column>
				<el-table-column prop="createTime" label="创建时间" width="150">
				</el-table-column>
				<el-table-column prop="createTime" label="操作" width="100" fixed="right">
					<template slot-scope="scope">
						<el-button size="mini" type="danger" :disabled="!isAuth('locality:delete')"
							@click="deleteuser(scope.row)">删除</el-button>
					</template>
				</el-table-column>

			</el-table>
			<div style="text-align: center;margin-top: 10px;">
				<el-pagination @size-change="handleSizeChangeLtnr" @current-change="handleCurrentChangeLtnr"
					:page-sizes="[10, 20, 30, 50, 100]" :page-size="limitB" :current-page="pageB"
					layout="total,sizes, prev, pager, next" :total="userData.totalCount">
				</el-pagination>
			</div>
		</el-dialog>
	</div>
</template>

<script>
	import {
		quillEditor
	} from 'vue-quill-editor'
	import 'quill/dist/quill.core.css'
	import 'quill/dist/quill.snow.css'
	import 'quill/dist/quill.bubble.css'
	import quillConfig from '../selfShop/quill-config.js'
	import axios from 'axios';
	import {
		jsonp
	} from 'vue-jsonp'
	import {
		provinceAndCityData,
		regionData,
		provinceAndCityDataPlus,
		regionDataPlus,
		CodeToText,
		TextToCode
	} from 'element-china-area-data'
	var cityOptions = []
	var geocoder, map, markersArray = [];
	export default {
		components: {
			quillEditor
		},
		data() {
			return {
				dialogWidth: 0,
				size: 10,
				page: 1,
				size1: 10,
				page1: 1,
				size2: 10,
				page2: 1,
				state: '',
				limit: 10,
				classifys: 6,
				openValue: 0,
				closeValue: 2,
				openValue1: '0',
				closeValue1: '1',
				openValue2: 0,
				closeValue2: 1,
				openValue3: 1,
				closeValue3: 2,
				title: '',
				type: '',
				name: '',
				keyword: '',
				describes: '',
				checkBoxData: [], //多选框选择的值
				method: 'false',
				formLabelWidth: '200px',
				activeName: 'third',
				tableDataLoading: true,
				tableDataLoading2: false,
				tableDataLoading4: false,
				tableDataLoadingB: false,
				dialogFormVisible3: false,
				dialogFormVisible1: false,
				dialogFormVisible2: false,
				dialogFormVisible5: false,
				dialogFormVisible6: false,
				dialogFormVisible7: false,
				dialogFormVisible8: false,
				dialogFormVisible9: false,
				dialogFormVisible10: false,
				dialogImageUrl: [],
				homeData: [],
				homeData1: [{
					id: 0,
					name: '全部'
				}],
				classifyList: [{
						id: '',
						name: '全部'
					},
					{
						id: 1,
						name: '线上'
					},
					{
						id: 2,
						name: '线下'
					}
				],
				choicenData: [],
				pinglunData: {},
				renwuId: '',
				url: '',
				imageUrl: '',
				id: '',
				userId: '',
				userNameT: '',
				activityNameT: '',
				shopNameT: '',
				cityT: '',
				phoneT: '',
				classifyIds: '',
				statusT: '',
				statuss: [{ //1报名中 2进行中 3已结束  5已取消  6待开始 7待审核 8已拒绝
						id: '',
						title: '全部'
					},
					{
						id: 1,
						title: '报名中'
					},
					{
						id: 2,
						title: '进行中'
					},
					{
						id: 3,
						title: '已结束'
					},
					{
						id: 5,
						title: '已取消'
					},
					{
						id: 6,
						title: '待开始'
					},
					{
						id: 7,
						title: '待审核'
					},
					{
						id: 8,
						title: '已拒绝'
					}
				],
				activityTypeT: '',
				gameIdT: '',
				activeList: [{
						id: '',
						title: '全部'
					},
					{
						id: 1,
						title: '猫鼠游戏'
					},
					{
						id: 2,
						title: '其他活动'
					}
				],
				// 发布信息
				activityName: '', //活动名称
				activityMan: '', //男生人数
				userIdss: '', //用户id
				activityGirl: '', //女生人数
				cat: '', //猫数量
				mouse: '', //鼠数量
				giftMouseNum: '', //礼物鼠
				snacksMouseNum: '', //零食鼠
				lifeMouseNum: '', //生命鼠
				infectMouseNum: '', //感染鼠
				kissMouseNum: '', //亲亲鼠
				convertMouseNum: '', //转换鼠
				beautyMouseNum: '', //美人鼠
				talentMouseNum: '', //才艺鼠
				flyMouseNum: '', //飞飞鼠
				pkMouseNum: '', //pk鼠
				randomNum: '', //随机身份

				roleMinute: '', //随机身份转变时间
				isSex: 2,
				activityImg: [], //主图
				activityDetailsImg: [], //活动展示图
				percentage: 0, //进度条
				percentage2: 0, //进度条
				voiceIntroduce: '', //语音介绍
				gameId: '', //游戏id
				areaId: '', //活动区域Id
				dizhiType: 1,
				areaData: [],
				activityType: 2, //活动分类
				activityPhone: '', //联系人电话
				price: '', //男生支付金额
				girlPrice:'',//女生支付金额
				activityAddress: '', //详细地址
				latitude: '', //纬度
				longitude: '', //经度
				city: '', //市
				orderTakingTime: '',
				status: '', //状态0待审核  1上架  2下架 3驳回 (修改添加不传)
				phone: '', //电话
				phone1: '',
				pickeroptions: {
					selectableRange: '00:00:01 - 23:59:59'
				},
				activityDate: '',
				activityEndDate: '',

				options: regionData,
				storeAddress: [],
				storeAddre: '请选择城市',
				province: '', //省
				district: '', //区
				ids: '', //信息id
				nickName: '',
				nickName1: '',
				nickName2: '',
				userIds: {}, //用户列表
				bq: '', //输入的标签名
				missions: 0,
				shenheId: 0, //审核id
				contents: '', //驳回理由
				dialogVisible: false,
				radio: 0,
				multipleSelection: [],
				campus: '',
				campus1: '',
				campusName: '',
				homeData2: [],
				quillOption: quillConfig,
				statusIdd: 1,
				tableData: {},
				renwuTitles: '添加活动',
				gameName: '',
				activityContent: '', //活动规则
				activityDetails: '', //活动须知
				address: '',
				maxDistance: '',
				dialogFormVisibleBq: false,
				pageB: 1,
				limitB: 50,
				biaoqianData: {},
				dialogFormVisibleBqadd: false,
				image: '',
				formBq: 1,
				isWangZhe: [],
				gameType: 1,
				activityId: '',
				pageB: 1,
				limitB: 10,
				baomingData: {},
				dialogFormVisibleQd: false,
				dialogFormVisibleXc: false,
				dialogFormVisibleZq: false,
				dialogFormVisibleJssj: false,
				activityEndTime: '',
				dialogFormVisibleBh: false,
				dialogFormVisibleSh: false,
				auditContent: '',
				statusSh: '',
				dialogFormVisible: false,
				userData: {},
				content: '',
				isFabuGame: 1,
				isBaomingfei: 1,
				phoneB: '',
				shopNameB: '',
				shopId: '',
				shopName: '',

				// 新建地址
				dialogFormVisibleDz: false,
				addressName: '',
				isEnable: '',
				remarks: '',
				province: '', //省
				city: '', //市
				district: '', //区
				address: '', //详细地址
				latitude: '', //纬度
				longitude: '', //经度
				options: regionData,
				storeAddress: [],
				storeAddre: '请选择城市',
				flag: '',
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
				info1: {
					stockDate1: this.getNowTime1(), //日期
				},
				times: '',
				totalMoney: 0,
				totalBaoming: 0,
				totalQiandao: 0,
				shopName: '',
				paymentType:1,
				priceType:2,
				tableDataVip:[],
				vipIndex:'',
			}
		},
		methods: {
			//处理默认选中当前日期
			getNowTime1() {
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
			// 日期选择3
			animeDat3(value) {
				let vanumber = value
				var datas = this.info1.stockDate1
				if (vanumber === 1) {
					this.flag = 1
					this.times = datas
					this.InformationSelect()
				}
				if (vanumber === 2) {

					this.flag = 2
					this.times = datas.slice(0, 7)
					this.InformationSelect()
				}
				if (vanumber === 3) {
					this.flag = 3
					this.times = datas.slice(0, 4)
					this.InformationSelect()

				}
			},
			animeSelect3() {
				var datas = this.info1.stockDate1
				console.log('时间：', this.info1.stockDate1)
				if (this.flag == 1) {
					console.log(datas)
					this.times = datas
					this.InformationSelect()
				}
				if (this.flag == 2) {
					console.log(datas.slice(0, 7))
					this.times = datas.slice(0, 7)
					this.InformationSelect()
				}
				if (this.flag == 3) {
					console.log(datas.slice(0, 4))
					this.times = datas.slice(0, 4)
					this.InformationSelect()

				}

			},
			handleSizeChange1(val) {
				this.size = val
				this.InformationSelect()
			},
			handleCurrentChange1(val) {
				this.page = val
				this.InformationSelect()
			},
			handleSizeChange2(val) {
				this.size1 = val
				this.tableDataLoading4 = true
				this.userClass()
			},
			handleCurrentChange2(val) {
				this.page1 = val
				this.tableDataLoading4 = true
				this.userClass()
			},
			handleSizeChangeBm(val) {
				this.limitB = val
				this.baomingSelect()
			},
			handleCurrentChangeBm(val) {
				this.pageB = val
				this.baomingSelect()
			},
			handleSizeChangeQd(val) {
				this.limitB = val
				this.qiandaoSelect()
			},
			handleCurrentChangeQd(val) {
				this.pageB = val
				this.qiandaoSelect()
			},
			handleSizeChangeXc(val) {
				this.limitB = val
				this.xiangceSelect()
			},
			handleCurrentChangeXc(val) {
				this.pageB = val
				this.xiangceSelect()
			},
			handleSizeChangeBh(val) {
				this.limitB = val
				this.buhuoSelect()
			},
			handleCurrentChangeBh(val) {
				this.pageB = val
				this.buhuoSelect()
			},
			handleSizeChangeLtnr(val) {
				this.limitB = val;
				this.neirongSelect(this.activityId)
			},
			handleCurrentChangeLtnr(val) {
				this.pageB = val;
				this.neirongSelect(this.activityId)
			},
			handleClick(tab, event) {
				this.campus = ''
				this.campus1 = ''
				this.page = 1
				if (tab._props.label == '活动列表') {
					this.type = 2
					this.InformationSelect()
				}

			},

			//添加活动
			choiaddNotice() {
				this.campus = ''
				this.campus1 = ''
				this.renwuTitles = '添加活动'
				this.dialogFormVisible5 = true
				this.percentage = 0
				this.gameId = ''
				this.areaId = ''
				this.dizhiType = 1
				this.activityType = 2
				this.activityName = ''
				this.orderTakingTime = ''
				this.activityDate = ''
				this.activityEndDate = ''
				this.activityMan = ''
				this.activityGirl = ''
				this.activityImg = []
				this.activityDetailsImg = []
				this.activityPhone = ''
				this.activityContent = ''
				this.maxDistance = ''
				this.activityDetails = ''
				this.price = ''
				this.girlPrice = ''
				this.paymentType = 1
				this.priceType = 2
				this.activityAddress = ''
				this.latitude = ''
				this.longitude = ''
				this.userIdss = ''
				this.giftMouseNum = ''

				// this.cat = ''
				// this.mouse = ''
				// this.snacksMouseNum = ''
				// this.lifeMouseNum = ''
				// this.infectMouseNum = ''
				// this.randomNum = ''
				// this.kissMouseNum = ''
				// this.convertMouseNum = ''
				// this.beautyMouseNum = ''
				// this.talentMouseNum = ''
				// this.flyMouseNum = ''
				// this.pkMouseNum = ''

				this.vipIndex = 0
				this.shopName = ''
				this.shopId = ''

				this.isSex = 2
				this.roleMinute = ''

				this.storeAddress = []
				this.storeAddre = '请选择城市'
				this.province = '' //省
				this.city = ''
				this.district = '' //区
				this.nickName1 = ''
				this.nickName2 = ''
				this.phone = ''
				this.ids = ''
				this.homeSelect()
				this.dataSelectVip()
				// this.areaSelect()
				// this.getMyLocation()
			},
			// 查询
			phoneSelect() {
				this.page = 1
				this.InformationSelect()
			},
			phoneSelectT() {
				this.page = 1
				this.gameIdT = ''
				this.homeSelectT()
				this.InformationSelect()
			},
			// 重置
			cleans2() {
				this.statusT = ''
				this.activityNameT = ''
				this.cityT = ''
				this.phoneT = ''
				this.userNameT = ''
				this.classifyIds = ''
				this.activityTypeT = ''
				this.gameIdT = ''
				this.flag = ''
				this.times = ''
				this.shopNameT = ''
				this.info1.stockDate1 = this.getNowTime1()
				this.page = 1
				this.InformationSelect()
				this.xianshi()
			},
			// 获取活动类型启用列表
			homeSelectT() {

				this.$http({
					url: this.$http.adornUrl(`game/queryGame`),
					method: 'get',
					params: this.$http.adornParams({
						'activityType': this.activityTypeT
					})
				}).then(({
					data
				}) => {
					let returnData = data.data
					this.homeData1 = data.data
					// if (data.data) {
					// 	for (var i in data.data) {
					// 		this.homeData1.push(data.data[i])
					// 		data.data[i].state = Number(data.data[i].state)
					// 	}
					// }
					// this.homeData = data.data
				})


			},
			homeSelect() {


				this.$http({
					url: this.$http.adornUrl(`game/queryGame`),
					method: 'get',
					params: this.$http.adornParams({
						'activityType': this.activityType
					})
				}).then(({
					data
				}) => {
					let returnData = data.data
					// this.homeData1 = [{
					// 	id: '',
					// 	gameName: '全部'
					// }]
					// if (data.data) {
					// 	for (var i in data.data) {
					// 		this.homeData1.push(data.data[i])
					// 		data.data[i].state = Number(data.data[i].state)
					// 	}
					// }
					this.homeData = data.data
				})

			},
			activityTypeBtn() {
				this.gameId = ''
				this.homeSelect()
			},
			// 获取活动区域
			areaSelect() {

				this.$http({
					url: this.$http.adornUrl('admin/banArea/getBanAreaList'),
					method: 'get',
					params: this.$http.adornParams({
						'remarks': ''
					})
				}).then(({
					data
				}) => {
					let returnData = data.data
					this.areaData = data.data.records
				})
			},
			// 信息数据
			InformationSelect() {
				this.tableDataLoading = true
				this.totalMoney = 0
				this.totalBaoming = 0
				this.totalQiandao = 0
				this.$http({
					url: this.$http.adornUrl('activity/selectActivityList'),
					method: 'get',
					params: this.$http.adornParams({
						'page': this.page,
						'limit': this.size,
						'userName': this.userNameT,
						'phone': this.phoneT,
						'status': this.statusT,
						'activityName': this.activityNameT,
						'city': this.cityT,
						'activityType': this.activityTypeT,
						'gameId': this.gameIdT,
						'time': this.times,
						'shopName': this.shopNameT,
					})
				}).then(({
					data
				}) => {
					this.tableDataLoading = false
					for (var i in data.data.list) {
						if (data.data.list[i].price) {
							this.totalMoney = this.totalMoney + Number(data.data.list[i].price)
						}
						if (data.data.list[i].girlPrice) {
							this.totalMoney = this.totalMoney + Number(data.data.list[i].girlPrice)
						}
						if (data.data.list[i].counts) {
							this.totalBaoming = this.totalBaoming + Number(data.data.list[i].counts)
						}
						if (data.data.list[i].signCounts) {
							this.totalQiandao = this.totalQiandao + Number(data.data.list[i].signCounts)
						}
					}
					let returnData = data.data
					this.choicenData = returnData
				})
			},
			// 封面图片上传
			handleAvatarSuccess5(file) {
				this.activityImg = file.data;
			},
			onprogress(event, file, fileList) {
				console.log('event, file, fileList', parseInt(event.percent))
				this.percentage = parseInt(event.percent)
			},

			onprogress2(event, file, fileList) {
				console.log('event, file, fileList', parseInt(event.percent))
				this.percentage2 = parseInt(event.percent)
			},
			// 详情图片上传
			handleRemove(file, ) {
				this.activityImg.push(file.data);
			},
			// 删除详情图
			dels(index) {
				console.log(this.activityImg)
				this.activityImg.splice(index, 1);

			},
			// 详情图片上传
			handleRemove2(file, ) {
				this.activityDetailsImg.push(file.data);
			},
			// 删除详情图
			dels2(index) {
				console.log(this.activityDetailsImg)
				this.activityDetailsImg.splice(index, 1);

			},

			// 获取省市区
			handleChange55(value) {
				value = this.$refs['cascaderAddr'].currentLabels
				this.province = value[0]
				this.city = value[1]
				this.district = value[2]
				if (this.city == '市辖区') {
					this.city = this.province
				}

				console.log(this.$refs['cascaderAddr'].currentLabels)
			},
			//定位获得当前位置信息
			getMyLocation() {

				var geolocation = new qq.maps.Geolocation("G5GBZ-NJ664-5KUUL-FBXE5-PQ5HK-C4BPJ", "充电桩");

				geolocation.getIpLocation(this.showPosition, this.showErr);
				// geolocation.getLocation(this.showPosition, this.showErr);//或者用getLocation精确度比较高
			},
			showPosition(position) {
				console.log(position);
				// this.latitude = position.lat;
				// this.longitude = position.lng;
				// this.city = position.city;
				this.setMap();
			},
			showErr(e) {
				console.log("定位失败", e);
				this.getMyLocation(); //定位失败再请求定位，测试使用
			},
			//位置信息在地图上展示
			setMap() {
				//步骤：定义map变量 调用 qq.maps.Map() 构造函数   获取地图显示容器
				//设置地图中心点
				var myLatlng = new qq.maps.LatLng(this.latitude, this.longitude);
				//定义工厂模式函数
				var myOptions = {
					zoom: 13, //设置地图缩放级别
					center: myLatlng, //设置中心点样式
					mapTypeId: qq.maps.MapTypeId.ROADMAP //设置地图样式详情参见MapType
				}
				// //获取dom元素添加地图信息
				var map = new qq.maps.Map(document.getElementById("container1"), myOptions);
				//给地图添加点击事件

				//给定位的位置添加图片标注
				var marker = new qq.maps.Marker({
					position: myLatlng,
					map: map
				});
				// `````````````
				var that = this;
				if (that.longitude == '') {
					var center = new qq.maps.LatLng(34.34281541842994, 108.93970884382725);
				} else {
					var center = new qq.maps.LatLng(that.latitude, that.longitude);
				}

				var map = new qq.maps.Map(document.getElementById("container1"), {
					center: center,
					zoom: 13
				});
				var marker = new qq.maps.Marker({
					position: center,
					map: map
				});
				var latlngBounds = new qq.maps.LatLngBounds();
				qq.maps.event.addListener(map, "click", function(event) {
					console.log(event, qq.maps);
					that.longitude = event.latLng.getLng(); // 经度
					that.latitude = event.latLng.getLat(); // 纬度

					jsonp('https://apis.map.qq.com/ws/geocoder/v1/?location=' + event.latLng.getLat() + ',' + event
						.latLng.getLng() + '&key=G5GBZ-NJ664-5KUUL-FBXE5-PQ5HK-C4BPJ&get_poi=1&output=jsonp', {
							myCustomUrlParam: 'veryNice'
						}).then(response => {
						console.log('response', response, response.result.address_component.city)
						that.address = response.result.formatted_addresses.recommend
						that.city = response.result.address_component.city
					}).catch(error => {
						// handle error
					}).then(() => {
						// always executed
					});
					if (markersArray) {
						for (let i in markersArray) {
							markersArray[i].setMap(null);
						}
					}
					if (!marker) {
						marker = new qq.maps.Marker({
							map: map,
							position: event.latLng
						});
					} else {
						marker.setPosition(event.latLng)
					}

					// markersArray.push(marker);

				});
				geocoder = new qq.maps.Geocoder({
					complete: function(result) {
						console.log(result);
						that.longitude = result.detail.location.lng;
						that.latitude = result.detail.location.lat;
						map.setCenter(result.detail.location);
						var marker = new qq.maps.Marker({
							map: map,
							position: result.detail.location
						});
						markersArray.push(marker);
					}
				});
				console.log('-----------------', that.city, )
			},
			// 地图定位
			select() {
				console.log(this.city, )

				// if (this.city == '') {
				// 	this.$notify({
				// 		title: '提示',
				// 		duration: 1800,
				// 		message: '请在地图上定位获取经纬度',
				// 		type: 'warning'
				// 	});
				// 	return
				// } else {
				var add = this.province + this.city + this.district + this.activityAddress
				let that = this
				jsonp('https://apis.map.qq.com/ws/geocoder/v1/?address==' + add +
					'&key=G5GBZ-NJ664-5KUUL-FBXE5-PQ5HK-C4BPJ&get_poi=1&output=jsonp', {
						myCustomUrlParam: 'veryNice'
					}).then(response => {
					// handle success
					if (response.message == '查询无结果') {
						this.$notify({
							title: '提示',
							duration: 1800,
							message: '详细地址输入有误，请重新输入',
							type: 'warning'
						});
						return
					}
					console.log('response', response)
					that.longitude = response.result.location.lng; // 经度
					that.latitude = response.result.location.lat; // 纬度
					that.city = response.result.address_components.city;
					// that.address = response.result.address_components.province + response.result.address_components
					// 	.city + response.result.address_components.district + response.result.title
					that.setMap()
				}).catch(error => {
					// handle error
				}).then(() => {
					// always executed
				});
				// }

			},
			// 获取用户列表弹框
			userselect() {
				this.dialogFormVisible3 = true
				this.tableDataLoading4 = true
				this.userClass()
			},
			// 获取用户列表
			userClass() {
				this.$http({
					// url: this.$http.adornUrl('userCertification/queryUserCertification'),
					// method: 'get',
					// params: this.$http.adornParams({
					// 	'page': this.page1,
					// 	'limit': this.size1,
					// 	'name': this.nickName2,
					// 	'phone': this.phone1
					// })

					url: this.$http.adornUrl('user/selectUserList'),
					method: 'get',
					params: this.$http.adornParams({
						'page': this.page1,
						'limit': this.size1,
						'member': -1,
						'userName': this.nickName2,
						'inviterCode': '',
						'startTime': '',
						'endTime': '',
						'isAuthentication': -1,
						'phone': this.phone1,
						'isActivity': 1,
					})
				}).then(({
					data
				}) => {
					this.tableDataLoading4 = false
					let returnData = data.data
					this.userIds = returnData
				})
			},
			// 查询用户列表
			userclick() {
				this.page = 1
				this.tableDataLoading4 = true
				this.userClass()
			},
			// 重置用户列表数据
			userclose() {
				this.phone1 = ''
				this.nickName2 = ''
				this.tableDataLoading4 = true

				this.userClass()
			},
			// 确定用户
			confirm(row) {
				this.userIdss = row.userId
				this.nickName1 = row.userName
				this.phone = row.phone
				// if (this.nickName == '' || this.nickName == null) {
				// 	this.nickName = row.phone
				// }
				this.dialogFormVisible3 = false
			},
			// 确定添加
			addmissionNoticeTo() {
				console.log(this.userIdss)

				// if (this.activityType === '') {
				// 	this.$notify({
				// 		title: '提示',
				// 		duration: 1800,
				// 		message: '请选择活动分类',
				// 		type: 'warning'
				// 	});
				// 	return
				// }
				// if (this.gameId === '') {
				// 	this.$notify({
				// 		title: '提示',
				// 		duration: 1800,
				// 		message: '请选择活动类型',
				// 		type: 'warning'
				// 	});
				// 	return
				// }
				if (this.activityType == 1) {
					if (this.areaId === '') {
						this.$notify({
							title: '提示',
							duration: 1800,
							message: '请选择活动区域',
							type: 'warning'
						});
						return
					}
				}


				if (this.shopId === '') {
					this.$notify({
						title: '提示',
						duration: 1800,
						message: '请选择商家',
						type: 'warning'
					});
					return
				}
				if (this.userIdss === '') {
					this.$notify({
						title: '提示',
						duration: 1800,
						message: '请选择发布人',
						type: 'warning'
					});
					return
				}

				if (this.activityName == '') {
					this.$notify({
						title: '提示',
						duration: 1800,
						message: '请输入活动名称',
						type: 'warning'
					});
					return
				}
				if (this.activityName.length > 50) {
					this.$notify({
						title: '提示',
						duration: 1800,
						message: '活动名称长度不得高于50个字',
						type: 'warning'
					});
					return
				}
				if (this.isSex === '' || this.isSex == null) {
					this.$notify({
						title: '提示',
						duration: 1800,
						message: '请选择是否限制男女',
						type: 'warning'
					});
					return
				}
				
				if (this.isSex == 1) {
					
					if (this.activityMan == null) {
						this.$notify({
							title: '提示',
							duration: 1800,
							message: '请输入男生人数',
							type: 'warning'
						});
						return
					}
				} else if (this.isSex == 2) {
					if (this.activityGirl === '') {
						this.$notify({
							title: '提示',
							duration: 1800,
							message: '请输入女生人数',
							type: 'warning'
						});
						return
					}
				}else{
					if (this.activityGirl === '') {
						this.$notify({
							title: '提示',
							duration: 1800,
							message: '请输入总人数',
							type: 'warning'
						});
						return
					}
					this.activityMan = 0
				}


				


				if (this.activityDate == '') {
					this.$notify({
						title: '提示',
						duration: 1800,
						message: '请选择活动开始时间',
						type: 'warning'
					});
					return
				}
				if (this.activityEndDate == '') {
					this.$notify({
						title: '提示',
						duration: 1800,
						message: '请选择报名截止时间',
						type: 'warning'
					});
					return
				}
				let date1 = new Date(this.activityDate);
				let date2 = new Date(this.activityEndDate);
				// if (date1.getTime() < date2.getTime()) {
				// 	this.$notify({
				// 		title: '提示',
				// 		duration: 1800,
				// 		message: '活动时间必须大于报名截止时间',
				// 		type: 'warning'
				// 	});
				// 	return
				// }

				if (this.activityImg.length == 0) {
					this.$notify({
						title: '提示',
						duration: 1800,
						message: '请上传主图',
						type: 'warning'
					});
					return
				}
				if (this.activityDetailsImg.length == 0) {
					this.$notify({
						title: '提示',
						duration: 1800,
						message: '请上传活动展示图',
						type: 'warning'
					});
					return
				}
				if (this.activityPhone == '') {
					this.$notify({
						title: '提示',
						duration: 1800,
						message: '请输入联系人电话',
						type: 'warning'
					});
					return
				}
				if (this.activityPhone.length >12) {
					this.$notify({
						title: '提示',
						duration: 1800,
						message: '联系人电话必须为手机号',
						type: 'warning'
					});
					return
				}
				if (this.paymentType === '') {
					this.$notify({
						title: '提示',
						duration: 1800,
						message: '请选择费用类型',
						type: 'warning'
					});
					return
				}
				// if (this.isBaomingfei == 1) {
					// if (this.priceType == 1){
					// 	if (this.price === '') {
					// 		this.$notify({
					// 			title: '提示',
					// 			duration: 1800,
					// 			message: '请输入男生活动费用',
					// 			type: 'warning'
					// 		});
					// 		return
					// 	}
					// 	if (this.girlPrice === '') {
					// 		this.$notify({
					// 			title: '提示',
					// 			duration: 1800,
					// 			message: '请输入女生活动费用',
					// 			type: 'warning'
					// 		});
					// 		return
					// 	}
					// 	if (this.paymentType != 2) {
					// 		if (Number(this.price) < 0||Number(this.girlPrice) < 0) {
					// 			this.$notify({
					// 				title: '提示',
					// 				duration: 1800,
					// 				message: '男女生活动费用都不能小于0',
					// 				type: 'warning'
					// 			});
					// 			return
					// 		}
					// 		if (Number(this.girlPrice) == 0&&Number(this.price) == 0) {
					// 			this.$notify({
					// 				title: '提示',
					// 				duration: 1800,
					// 				message: '男女生活动费用必须有一方大于0',
					// 				type: 'warning'
					// 			});
					// 			return
					// 		}
					// 	}else{
					// 		this.girlPrice = 0
					// 		this.price = 0
					// 	}
						
					// }else{
						if (this.paymentType != 2&&this.paymentType != 4) {
							if (this.price === '') {
								this.$notify({
									title: '提示',
									duration: 1800,
									message: '请输入活动费用',
									type: 'warning'
								});
								return
							}
							if (this.price <= 0) {
								this.$notify({
									title: '提示',
									duration: 1800,
									message: '活动费用必须大于0',
									type: 'warning'
								});
								return
							}
							this.girlPrice = this.price
						}else{
							this.girlPrice = 0
							this.price = 0
						}
						
					// }
					

				// } else {
				// 	this.girlPrice = 0
				// 	this.price = 0
				// }

				// if (this.activityDetails == '') {
				// 	this.$notify({
				// 		title: '提示',
				// 		duration: 1800,
				// 		message: '请输入活动须知',
				// 		type: 'warning'
				// 	});
				// 	return
				// }
				// if (this.activityContent == '') {
				// 	this.$notify({
				// 		title: '提示',
				// 		duration: 1800,
				// 		message: '请输入活动规则',
				// 		type: 'warning'
				// 	});
				// 	return
				// }
				// if (this.maxDistance === '' || this.maxDistance == null) {
				// 	this.$notify({
				// 		title: '提示',
				// 		duration: 1800,
				// 		message: '请输入最大距离',
				// 		type: 'warning'
				// 	});
				// 	return
				// }

				// if (this.activityAddress == '') {
				// 	this.$notify({
				// 		title: '提示',
				// 		duration: 1800,
				// 		message: '请输入详细地址',
				// 		type: 'warning'
				// 	});
				// 	return
				// }

				// if (this.city == '') {
				// 	this.$notify({
				// 		title: '提示',
				// 		duration: 1800,
				// 		message: '请在地图上定位获取城市',
				// 		type: 'warning'
				// 	});
				// 	return
				// }
				// if (this.latitude == '' || this.longitude == '') {
				// 	this.$notify({
				// 		title: '提示',
				// 		duration: 1800,
				// 		message: '请在地图上定位获取经纬度',
				// 		type: 'warning'
				// 	});
				// 	return
				// }


				let urls = ''
				if (this.renwuTitles == '添加活动') {
					urls = 'activity/insertActivity'
				} else {
					urls = 'activity/updateActivity'
				}

				let that = this
				setTimeout(function() {
					that.$http({
						url: that.$http.adornUrl(urls),
						method: 'post',
						data: that.$http.adornData({
							// params: that.$http.adornParams({
							'gameId': that.gameId,
							'activityType': that.activityType,
							'activityName': that.activityName,
							'activityMan': that.activityMan,
							'activityGirl': that.activityGirl,
							'activityDate': that.activityDate,
							'activityEndDate': that.activityEndDate,
							'activityPhone': that.activityPhone,
							'price': that.price,
							'girlPrice':that.girlPrice,
							'activityAddress': that.activityAddress,
							'activityProvince': that.province,
							'activityCity': that.city,
							'activityDistrict': that.district,
							'activityLatitude': that.latitude,
							'activityLongitude': that.longitude,
							'activityImg': that.activityImg.toString(),
							'activityDetailsImg': that.activityDetailsImg.toString(),
							'userId': that.userIdss,
							'activityId': that.ids,
							'activityContent': that.activityContent,
							'activityDetails': that.activityDetails,
							'status': that.status,
							'cat': that.cat,
							'mouse': that.mouse,
							'maxDistance': that.maxDistance,
							'areaId': that.areaId,
							'giftMouseNum': that.giftMouseNum,
							'snacksMouseNum': that.snacksMouseNum,
							'lifeMouseNum': that.lifeMouseNum,
							'infectMouseNum': that.infectMouseNum,
							'randomNum': that.randomNum,
							'kissMouseNum': that.kissMouseNum,
							'convertMouseNum': that.convertMouseNum,
							'beautyMouseNum': that.beautyMouseNum,
							'talentMouseNum': that.talentMouseNum,
							'flyMouseNum': that.flyMouseNum,
							'pkMouseNum': that.pkMouseNum,
							'roleMinute': that.roleMinute,
							'isSex': that.isSex,
							'shopId': that.shopId,
							'paymentType':that.paymentType,
							'priceType':that.priceType,
							'vipIndex':that.vipIndex,


						})
					}).then(({
						data
					}) => {
						console.log('提交结果', data)
						if (data.code == 0) {
							that.$message({
								message: '操作成功',
								type: 'success',
								duration: 1500,
								onClose: () => {
									that.dialogFormVisible5 = false
									that.InformationSelect()
								}
							})
						} else {
							that.$message({
								title: '提示',
								duration: 1800,
								message: data.msg,
								type: 'warning'
							});
						}


					})
				}, 1000)
			},

			// 报名列表弹框
			shenhe(row) {
				this.activityId = row.activityId
				this.pageB = 1
				this.dialogFormVisible8 = true
				this.baomingSelect()
			},
			// 获取报名列表
			baomingSelect() {
				this.tableDataLoadingB = true
				this.$http({
					url: this.$http.adornUrl('activityApply/selectActivityApplyList'),
					method: 'get',
					params: this.$http.adornParams({
						'page': this.pageB,
						'limit': this.limitB,
						'activityId': this.activityId,
						'activityName': '',
						'userName': '',
						'phone': '',
						'status': '',
					})
				}).then(({
					data
				}) => {
					console.log('', data)
					this.tableDataLoadingB = false
					if (data.code == 0) {
						if (data.data.list.length > 0) {
							for (var i in data.data.list) {
								let payMoney = ''
								let zhiMoney = ''
								let feiMoney = ''
								if (data.data.list[i].payMoney) {
									payMoney = Number(data.data.list[i].payMoney)
								} else {
									payMoney = 0
								}
								if (data.data.list[i].zhiMoney) {
									zhiMoney = Number(data.data.list[i].zhiMoney)
								} else {
									zhiMoney = 0
								}
								if (data.data.list[i].feiMoney) {
									feiMoney = Number(data.data.list[i].feiMoney)
								} else {
									feiMoney = 0
								}
								data.data.list[i].pingMoney = (payMoney - zhiMoney - feiMoney).toFixed(2)
							}
						}
						let returnData = data.data
						this.baomingData = returnData
					} else {
						this.$notify({
							title: '提示',
							duration: 1800,
							message: data.msg,
							type: 'warning'
						});
					}
				})
			},
			// 提交审核
			refuseto(row) {
				if (this.radio == 3 && this.contents == '') {
					this.$notify({
						title: '提示',
						duration: 1800,
						message: '请输入驳回理由',
						type: 'warning'
					})
					return
				} else {
					let ids = this.shenheId
					let status = this.radio
					let content = this.contents
					this.$http({
						url: this.$http.adornUrl(`orderTaking/auditorOrderTaking`),
						method: 'get',
						params: this.$http.adornParams({
							'id': ids,
							'status': status,
							'content': content
						})
					}).then(({
						data
					}) => {
						this.dialogFormVisible8 = false
						this.$message({
							message: '审核完成',
							type: 'success',
							duration: 1500,
							onClose: () => {
								this.radio = 0
								this.contents = ''
								this.InformationSelect()
							}
						})
					})
				}
			},
			// 修改信息弹框
			choiCompile(row) {
				console.log(row)
				this.labels = []
				this.missions = 1
				this.renwuTitles = '修改活动'
				if (row) {
					this.gameId = row.gameId
					if (row.areaId) {
						this.areaId = row.areaId
					} else {
						this.areaId = ''

					}
					this.dizhiType = 1
					this.activityType = row.activityType
					this.activityName = row.activityName
					this.activityMan = row.activityMan
					this.activityGirl = row.activityGirl
					this.cat = row.cat
					this.mouse = row.mouse
					this.giftMouseNum = row.giftMouseNum
					this.snacksMouseNum = row.snacksMouseNum
					this.lifeMouseNum = row.lifeMouseNum
					this.infectMouseNum = row.infectMouseNum

					this.kissMouseNum = row.kissMouseNum
					this.convertMouseNum = row.convertMouseNum
					this.beautyMouseNum = row.beautyMouseNum
					this.talentMouseNum = row.talentMouseNum
					this.flyMouseNum = row.flyMouseNum
					this.pkMouseNum = row.pkMouseNum

					this.randomNum = row.randomNum
					this.isSex = row.isSex
					this.roleMinute = row.roleMinute
					this.orderTakingTime = row.orderTakingTime
					this.activityDate = row.activityDate
					this.activityEndDate = row.activityEndDate
					this.activityPhone = row.activityPhone
					this.price = row.price
					this.girlPrice = row.girlPrice
					this.activityAddress = row.activityAddress
					this.storeAddress = []
					this.storeAddre = row.activityProvince + '/' + row.activityCity + '/' + row.activityDistrict
					this.province = row.activityProvince
					this.city = row.activityCity
					this.district = row.activityDistrict
					this.latitude = row.activityLatitude
					this.longitude = row.activityLongitude
					if(row.activityImg){
						this.activityImg = row.activityImg.split(',')
					}else{
						this.activityImg = []
					}
					if(row.activityDetailsImg){
						this.activityDetailsImg = row.activityDetailsImg.split(',')
					}else{
						this.activityDetailsImg = []
					}
					
					
					this.ids = row.activityId
					this.nickName1 = row.userName
					this.userIdss = row.userId
					this.status = Number(row.status)
					this.percentage = 100
					this.activityContent = row.activityContent
					this.activityDetails = row.activityDetails
					this.maxDistance = row.maxDistance
					this.shopId = row.shopId
					this.shopName = row.shopName
					this.paymentType = row.paymentType
					if(row.priceType){
						this.priceType = row.priceType
					}else{
						this.priceType = 2
					}
					if(row.vipIndex){
						this.vipIndex = row.vipIndex
					}else{
						this.vipIndex = 0
					}
					
				}
				this.dialogFormVisible5 = true
				this.homeSelect()
				// this.areaSelect()
				this.getMyLocation()
				this.dataSelectVip()
			},

			// 删除信息
			choidelete(row) {
				this.$confirm(`确定删除此条信息?`, '提示', {
					confirmButtonText: '确定',
					cancelButtonText: '取消',
					type: 'warning'
				}).then(() => {
					this.$http({
						url: this.$http.adornUrl('activity/deleteActivityById'),
						method: 'post',
						params: this.$http.adornParams({
							'activityId': row.activityId
						})
					}).then(({
						data
					}) => {
						if (data.code == 0) {
							this.$message({
								message: '删除成功',
								type: 'success',
								duration: 1500,
								onClose: () => {
									this.InformationSelect()
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
				}).catch(() => {})
			},

			// 详情跳转
			updatesvideo(userId) {
				if (userId) {
					this.$router.push({
						path: '/userDetail',
						query: {
							userId: userId
						}
					})
				}

			},

			// 获取活动类型数据
			classSelect() {
				this.$http({
					url: this.$http.adornUrl('game/queryGameName'),
					method: 'get',
					params: this.$http.adornParams({
						'page': this.page,
						'limit': this.limit
					})
				}).then(({
					data
				}) => {
					console.log('', data)
					if (data.code == 0) {
						let returnData = data.data
						this.tableData = returnData
					} else {
						this.$notify({
							title: '提示',
							duration: 1800,
							message: data.msg,
							type: 'warning'
						});
					}
				})
			},
			// 选择类型
			gameBtn() {
				for (var i in this.homeData) {
					if (this.gameId == this.homeData[i].id) {
						if (this.homeData[i].gameDetails) {
							this.activityContent = this.homeData[i].gameDetails
						} else {
							this.activityContent = ''
						}
					}
				}
			},
			areaBtn() {
				for (var i in this.areaData) {
					if (this.areaId == this.areaData[i].areaId) {
						this.activityAddress = this.areaData[i].address
						this.province = this.areaData[i].province
						this.city = this.areaData[i].city
						this.district = this.areaData[i].district
						this.latitude = this.areaData[i].latitude
						this.longitude = this.areaData[i].longitude
						// if (this.areaData[i].gameDetails) {
						// 	this.activityContent = this.areaData[i].gameDetails
						// } else {
						// 	this.activityContent = ''
						// }
					}
				}
			},
			// 签到列表弹框
			qiandao(row) {
				this.activityId = row.activityId
				this.pageB = 1
				this.dialogFormVisibleQd = true
				this.qiandaoSelect()
			},
			// 获取签到列表
			qiandaoSelect() {
				this.tableDataLoadingB = true
				this.$http({
					url: this.$http.adornUrl(`message/selectActivitySignList`),
					method: 'get',
					params: this.$http.adornParams({
						'page': this.pageB,
						'limit': this.limitB,
						'state': 110,
						'platform': '',
						'activityId': this.activityId
					})
				}).then(({
					data
				}) => {
					console.log('', data)
					this.tableDataLoadingB = false
					if (data.code == 0) {
						let returnData = data.data
						this.baomingData = returnData
					} else {
						this.$notify({
							title: '提示',
							duration: 1800,
							message: data.msg,
							type: 'warning'
						});
					}


				})
			},
			// 活动相册弹框
			xiangce(row) {
				this.activityId = row.activityId
				this.pageB = 1
				this.dialogFormVisibleXc = true
				this.xiangceSelect()
			},
			xiangceSelect() {
				this.tableDataLoadingB = true
				this.$http({
					url: this.$http.adornUrl('activityPhoto/selectActivityPhotoList'),
					method: 'get',
					params: this.$http.adornParams({
						'page': this.pageB,
						'limit': this.limitB,
						'userName': '',
						'activityName': '',
						'phone': '',
						'activityId': this.activityId
					})
				}).then(({
					data
				}) => {
					console.log('', data)
					this.tableDataLoadingB = false
					if (data.code == 0) {
						let returnData = data.data
						this.baomingData = returnData
					} else {
						this.$notify({
							title: '提示',
							duration: 1800,
							message: data.msg,
							type: 'warning'
						});
					}
				})
			},
			choideleteDe(row) {
				this.$confirm(`确定删除此条信息?`, '提示', {
					confirmButtonText: '确定',
					cancelButtonText: '取消',
					type: 'warning'
				}).then(() => {
					this.$http({
						url: this.$http.adornUrl('activityPhoto/deleteActivityPhoto'),
						method: 'post',
						params: this.$http.adornParams({
							'activityPhotoId': row.activityPhotoId
						})
					}).then(({
						data
					}) => {
						if (data.code == 0) {
							this.$message({
								message: '删除成功',
								type: 'success',
								duration: 1500,
								onClose: () => {
									this.xiangceSelect()
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
				}).catch(() => {})
			},
			buhuoBtns() {
				this.pageB = 1
				this.buhuoSelect()
			},
			// 捕获记录
			buhuo(row) {
				this.activityId = row.activityId
				this.pageB = 1
				this.phoneB = ''
				this.dialogFormVisibleBh = true
				this.buhuoSelect()
			},

			// 最强详情
			zuiqiang(row) {
				this.activityId = row.activityId
				this.pageB = 1
				this.dialogFormVisibleZq = true
				this.zuiqiangSelect()
			},
			zuiqiangSelect() {
				this.tableDataLoadingB = true
				this.$http({
					url: this.$http.adornUrl('activity/selectMaxCatAndMouseList'),
					method: 'get',
					params: this.$http.adornParams({
						'activityId': this.activityId
					})
				}).then(({
					data
				}) => {
					console.log('', data)
					this.tableDataLoadingB = false
					if (data.code == 0) {
						let returnData = data.data
						this.baomingData = returnData
					} else {
						this.$notify({
							title: '提示',
							duration: 1800,
							message: data.msg,
							type: 'warning'
						});
					}
				})
				// this.$http({
				// 	url: this.$http.adornUrl('activityApply/selectActivityCatListByActivityList'),
				// 	method: 'get',
				// 	params: this.$http.adornParams({
				// 		'activityId': this.activityId,
				// 		'page': this.pageB,
				// 		'limit': this.limitB,
				// 	})
				// }).then(({
				// 	data
				// }) => {
				// 	// console.log('', data)
				// 	// this.tableDataLoadingB = false
				// 	// if (data.code == 0) {
				// 	// 	let returnData = data.data
				// 	// 	this.baomingData = returnData
				// 	// } else {
				// 	// 	this.$notify({
				// 	// 		title: '提示',
				// 	// 		duration: 1800,
				// 	// 		message: data.msg,
				// 	// 		type: 'warning'
				// 	// 	});
				// 	// }
				// })
			},
			// 选择结束时间
			jieshuBtn(row) {
				this.activityId = row.activityId
				this.activityDate = row.activityDate
				this.activityEndTime = ''
				this.dialogFormVisibleJssj = true

			},
			// 开始活动
			kaishi() {
				if (this.activityEndTime == '') {
					this.$notify({
						title: '提示',
						duration: 1800,
						message: '请选择活动结束时间',
						type: 'warning'
					});
					return
				}
				let date1 = new Date(this.activityDate);
				let date2 = new Date(this.activityEndTime);
				if (date1.getTime() > date2.getTime()) {
					this.$notify({
						title: '提示',
						duration: 1800,
						message: '活动结束时间必须大于活动开始时间',
						type: 'warning'
					});
					return
				}
				this.$confirm(`确定开始活动?`, '提示', {
					confirmButtonText: '确定',
					cancelButtonText: '取消',
					type: 'warning'
				}).then(() => {
					this.$http({
						url: this.$http.adornUrl('activity/startActivity'),
						method: 'post',
						params: this.$http.adornParams({
							'activityId': this.activityId,
							'activityEndTime': this.activityEndTime
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
									this.InformationSelect()
									this.dialogFormVisibleJssj = false
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
				}).catch(() => {})
			},
			// 取消活动
			quxiaoBtn(row) {
				this.$confirm(`确定取消活动?`, '提示', {
					confirmButtonText: '确定',
					cancelButtonText: '取消',
					type: 'warning'
				}).then(() => {
					this.$http({
						url: this.$http.adornUrl('activity/cancelActivity'),
						method: 'post',
						params: this.$http.adornParams({
							'activityId': row.activityId
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
									this.InformationSelect()
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
				}).catch(() => {})
			},
			// 停止报名
			tingzhi(row) {
				this.$confirm(`确定该活动停止报名?`, '提示', {
					confirmButtonText: '确定',
					cancelButtonText: '取消',
					type: 'warning'
				}).then(() => {
					this.$http({
						url: this.$http.adornUrl('activity/stopApplyActivity'),
						method: 'post',
						params: this.$http.adornParams({
							'activityId': row.activityId,
							'status': 6
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
									this.InformationSelect()
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
				}).catch(() => {})
			},
			// 结束活动
			jieshu(row) {
				this.$confirm(`确定结束该活动?`, '提示', {
					confirmButtonText: '确定',
					cancelButtonText: '取消',
					type: 'warning'
				}).then(() => {
					this.$http({
						url: this.$http.adornUrl('activity/endActivity'),
						method: 'post',
						params: this.$http.adornParams({
							'activityId': row.activityId,
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
									this.InformationSelect()
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
				}).catch(() => {})
			},
			// 活动地图
			dituBtn(row) {
				this.$router.push({
					path: '/shifuScheduling',
					query: {
						activityId: row.activityId,
						longitude: row.activityLongitude,
						latitude: row.activityLatitude,
						areaId: row.areaId
					}
				})
			},
			// 审核弹框
			shenhedelete(row) {
				this.activityId = row.activityId
				this.auditContent = ''
				this.statusSh = ''
				this.dialogFormVisibleSh = true
			},
			// 确定审核
			shenheBtn() {
				if (this.statusSh == '') {
					this.$notify({
						title: '提示',
						duration: 1800,
						message: '请选择审核结果',
						type: 'warning'
					});
					return
				}
				if (this.statusSh == 2) {
					if (this.auditContent == '') {
						this.$notify({
							title: '提示',
							duration: 1800,
							message: '请输入审核理由',
							type: 'warning'
						});
						return
					}
					if (this.auditContent.length > 100) {
						this.$notify({
							title: '提示',
							duration: 1800,
							message: '审核理由最多可输入100字',
							type: 'warning'
						});
						return
					}
				}

				this.$http({
					url: this.$http.adornUrl('activity/auditActivity'),
					method: 'post',
					params: this.$http.adornParams({
						'activityId': this.activityId,
						'status': this.statusSh,
						'auditContent': this.auditContent
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
								this.InformationSelect()
								this.dialogFormVisibleSh = false
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
			},
			// 聊天内容
			complainDetails(row) {
				this.pageB = 1
				this.activityId = row.activityId
				this.neirongSelect(this.activityId)
				this.dialogFormVisible = true
			},
			select1() {
				this.pageB = 1
				this.neirongSelect(this.activityId)
			},
			// 重置
			cleans1() {
				this.content = ''
				this.pageB = 1
				this.neirongSelect(this.activityId)
			},
			// 获取聊天内容列表
			neirongSelect(activityId) {
				this.tableDataLoading = true
				this.$http({
					url: this.$http.adornUrl('activityChat/selectChatContent'),
					method: 'get',
					params: this.$http.adornParams({
						'page': this.pageB,
						'limit': this.limitB,
						'activityId': this.activityId,
						'content': this.content
					})
				}).then(({
					data
				}) => {
					this.tableDataLoading = false
					let returnData = data.data;
					this.userData = returnData
				})
			},
			//删除聊天
			deleteuser(row) {
				// let delid = row.userId
				this.$confirm(`确定删除此条信息?`, '提示', {
					confirmButtonText: '确定',
					cancelButtonText: '取消',
					type: 'warning'
				}).then(() => {
					this.$http({
						url: this.$http.adornUrl(
							'activityChat/deleteChatContentById?activityChatContentId=' + row
							.activityChatContentId),
						method: 'post',
						params: this.$http.adornData({})
					}).then(({
						data
					}) => {
						if (data.code == 0) {
							this.$message({
								message: '删除成功',
								type: 'success',
								duration: 1500,
								onClose: () => {
									this.neirongSelect(this.activityId)
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
			// 是否开启发布游戏活动
			xianshi() {
				this.$http({
					url: this.$http.adornUrl('app/common/type/837'),
					method: 'get',
					data: this.$http.adornData({})
				}).then(({
					data
				}) => {
					if (data.code == 0) {
						if (data.data.value == '是') {
							this.isFabuGame = 1
						} else {
							this.isFabuGame = 2
						}
					}
				})
				this.$http({
					url: this.$http.adornUrl('app/common/type/838'),
					method: 'get',
					data: this.$http.adornData({})
				}).then(({
					data
				}) => {
					if (data.code == 0) {
						if (data.data.value == '是') {
							this.isBaomingfei = 1
						} else {
							this.isBaomingfei = 2
						}
					}
				})
			},
			// 围栏地图
			selectWl(row) {
				if (row) {
					this.$router.push({
						path: '/riderSchedulingFw',
						query: {
							contexts: JSON.stringify(row),
							from: 'loc'
						}
					})
				}
			},
			// 切换地址类型
			xjdzBtn() {
				this.storeAddre = '请选择省市区'
				this.province = ''
				this.city = ''
				this.district = ''
				this.activityAddress = ''
				this.latitude = ''
				this.longitude = ''
				if (this.activityType == 2) {
					this.areaId = ''
					let data = {
						key: "ZBABZ-ZWECU-UQTVV-4LYDR-COK3F-5SF75", //申请的密钥
						output: "jsonp",
					};
					let url = "https://apis.map.qq.com/ws/location/v1/ip"
					this.$jsonp(url, data).then(res => {
						console.log('获取Ip定位成功----', res.result.location)
						this.longitude = res.result.location.lng
						this.latitude = res.result.location.lat

					}).catch(err => {
						this.$toast('获取Ip定位失败--', err.message)
					})
					this.getMyLocation()
				}
			},
			// 新建地址
			addmissionNoticeToDz() {
				if (this.addressName == '') {
					this.$notify({
						title: '提示',
						duration: 1800,
						message: '请输入地址名称',
						type: 'warning'
					});
					return
				}
				if (this.isEnable === '') {
					this.$notify({
						title: '提示',
						duration: 1800,
						message: '请选择状态',
						type: 'warning'
					});
					return
				}
				if (this.remarks == '') {
					this.$notify({
						title: '提示',
						duration: 1800,
						message: '请输入备注',
						type: 'warning'
					});
					return
				}
				if (this.province == '' || this.city == '' || this.district == '') {
					this.$notify({
						title: '提示',
						duration: 1800,
						message: '请选择集合点地区',
						type: 'warning'
					});
					return
				}
				if (this.address == '') {
					this.$notify({
						title: '提示',
						duration: 1800,
						message: '请输入集合点详细地址',
						type: 'warning'
					});
					return
				}
				if (this.latitude == '' && this.longitude == '') {
					this.$notify({
						title: '提示',
						duration: 1800,
						message: '经度或纬度不能为空',
						type: 'warning'
					});
					return
				}
				this.$http({
					url: this.$http.adornUrl('admin/banArea/saveBanArea'),
					method: 'post',
					// params: this.$http.adornParams({
					data: this.$http.adornData({
						// 'points': this.points,
						'isEnable': this.isEnable,
						'remarks': this.remarks,
						'areaId': this.areaId,
						'addressName': this.addressName,
						'province': this.province,
						'city': this.city,
						'district': this.district,
						'address': this.address,
						'latitude': this.latitude,
						'longitude': this.longitude,
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
								this.dialogFormVisibleDz = false
								this.areaSelect()
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
			},
			// 商家弹框
			shopselect() {
				this.pageB = 1
				this.phoneB = ''
				this.shopNameB = ''

				this.dialogFormVisibleBh = true
				this.buhuoSelect()
			},
			buhuoSelect() {
				this.tableDataLoadingB = true
				this.$http({    //tearoom/selectTearoomList?page=1&limit=10&tearoomName=&userName=&city=&status=
					url: this.$http.adornUrl('tearoom/selectTearoomList'),
					method: 'get',
					params: this.$http.adornParams({
						'page': this.pageB,
						'limit': this.limitB,
						'userName': this.userNameT,
						// 'phone': this.phoneB,
						'tearoomName': this.shopNameB,
						'status': '',
						'city': '',

					})


				}).then(({
					data
				}) => {
					console.log('', data)
					this.tableDataLoadingB = false
					if (data.code == 0) {
						let returnData = data.data
						this.baomingData = returnData
					} else {
						this.$notify({
							title: '提示',
							duration: 1800,
							message: data.msg,
							type: 'warning'
						});
					}
				})
			},
			xzShop(row) {
				
				this.shopName = row.tearoomName
				this.shopId = row.tearoomId
				this.province = row.tearoomProvince
				this.city = row.tearoomCity
				this.district = row.tearoomDistrict
				this.activityAddress = row.tearoomAddress
				this.latitude = row.tearoomLatitude
				this.longitude = row.tearoomLongitude
				this.userIdss = row.userId
				this.dialogFormVisibleBh = false
			},
			paymentTypeNtm() {
				if (this.paymentType == 2) {
					this.price = 0
					this.girlPrice = 0
				}
			},
			dataSelectVip() {
				
				this.$http({
					url: this.$http.adornUrl('vip/selectVipList'),
					method: 'get',
					params: this.$http.adornParams({
						'page': 1,
						'limit': 20,
						'vipName': '',
						'isEnable':'',
					})
				}).then(({
					data
				}) => {
					let returnData = data.data
					this.tableDataVip = returnData.records
					var datas = {
						vipName:'不限等级',
						vipIndex:0,
					}
					this.tableDataVip.push(datas)
				})
			},
			dataSelect1() {
			  if(this.$route.query.status!=undefined&&this.$route.query.status){
			    this.statusT = Number(this.$route.query.status)
			  }else{
			    this.statusT = ''
			  }
			  this.InformationSelect()
			},
			// 推送消息
			tuisongBtn(row) {
				this.$confirm(`该活动确定要推送消息?`, '提示', {
					confirmButtonText: '确定',
					cancelButtonText: '取消',
					type: 'warning'
				}).then(() => {
					this.$http({
						url: this.$http.adornUrl('activity/sendMsg'),
						method: 'post',
						params: this.$http.adornParams({
							'activityId': row.activityId
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
									this.InformationSelect()
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
				}).catch(() => {})
			},
		},
		mounted() {
			if(this.$route.query.status!=undefined&&this.$route.query.status){
			  this.statusT = Number(this.$route.query.status)
			  this.InformationSelect()
			}else{
			  this.statusT = ''
			  this.InformationSelect()
			}
			
			
			this.homeSelect()
			this.homeSelectT()
			// this.areaSelect()
			this.xianshi()
		},
		watch: {
		  '$route': 'dataSelect1'
		}
	}
</script>

<style scoped>
.locality-page__filter-select {
	width: 150px;
}

.locality-page__filter-input {
	width: 200px;
}

.locality-page__filter-date {
	width: 200px;
}

.locality-page__filter-actions .el-button + .el-button {
	margin-left: 12px;
}

.locality-page__link {
	color: #5cc477;
	cursor: pointer;
}

.locality-page__link:hover {
	color: #3fae5c;
}

.locality-page__stats {
	display: flex;
	flex-wrap: wrap;
	gap: 16px;
	padding-top: 12px;
	margin-top: 12px;
	border-top: 1px solid #e8eaed;
}

.locality-page__stat-item {
	display: flex;
	align-items: baseline;
	gap: 4px;
	font-size: 14px;
	color: #606266;
}

.locality-page__stat-label {
	color: #909399;
}

.locality-page__stat-value {
	font-size: 20px;
	font-weight: 600;
	color: #5cc477;
}

.locality-page__stat-unit {
	color: #606266;
}

.locality-page__pagination {
	display: flex;
	justify-content: flex-end;
	padding-top: 12px;
}

.nf-table-card .el-button + .el-button {
	margin-left: 0 !important;
	margin-top: 5px !important;
}
</style>

<style>
	.customWidth {
		width: 80% !important;
	}

	.adver_main.box {
		display: block;
		max-width: 100%;
		text-align: center;
		border: 1px dotted rgba(67, 79, 103, .4);
	}

	.cards {
		padding: 0 8px;
		margin-bottom: 15px;
	}

	.adver_main.box a {
		display: flex;
		justify-content: center;
		height: 150px;
		line-height: 150px;
		text-decoration: none
	}

	.bannerManin {
		border: 1px solid #e8e8e8;
		font-size: 14px;
		padding: 0 24px;
		display: flex;
		justify-content: center;
		align-items: center;
		height: 113px;
		color: rgba(0, 0, 0, .65);
	}

	.bannerManin span {
		display: inline-block;
		margin-left: 5px;
	}

	.bannerManin img {
		width: 48px;
		height: 48px;
		border-radius: 50%;
	}

	.bannerbtn {
		display: flex;
		border-top: none !important;
		border: 1px solid #e8e8e8;
		padding: 11px;
		font-size: 14px;
		color: #5cc477;
	}

	.bannerbtn a {
		flex: 1;
		text-align: center;
		color: #5cc477 !important;
		text-decoration: none;
	}

	.imgs {
		position: relative;
		border-radius: 6px;
		width: 148px;
		height: 148px;
		margin-right: 10px;
		display: inline-block;
	}

	.dels {
		position: absolute;
		top: 0;
		left: 0;
		display: none;
	}

	.dels .el-icon-delete {
		line-height: 148px;
		padding-left: 58px;
		font-size: 25px;
		color: #fff;
	}

	.imgs:hover .dels {
		width: 100%;
		height: 100%;
		background: #000;
		display: block;
		opacity: 0.5;
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

	.biaoqianCla {
		position: relative;
		width: 90px;
		height: 75px;
		display: inline-block;
		padding: 10px;
		box-shadow: 0px 0px 7px #999;
		margin: 5px;
	}
</style>