<template>
	<u-empty :marginTop="marginTop" :text="content" :icon="icon">
	</u-empty>
</template>

<script>
	export default {
		props: {
			content: {
				type: String,
				default: '暂无数据'
			},
			marginTop: {
				type: String,
				default: '50%'
			},
			icon: {
				type: String,
				default: '/static/images/empty.png'
			}
		}
	}
</script>