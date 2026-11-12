import { ObjToStringQuery } from '@/utils'

export default {
  data() {
    return {
      imgUrl: this.$imgUrl,
      doctor_theme_color: '#006EFF'
    }
  },
  methods: {
    navigate(url, params = null) {
      if (params)
        url = `${url}${url.indexOf('?') === -1 ? '?' : '&'}${ObjToStringQuery(
          params
        )}`
      uni.navigateTo({ url })
    },
    redirect(url, params = null) {
      if (params)
        url = `${url}${url.indexOf('?') === -1 ? '?' : '&'}${ObjToStringQuery(
          params
        )}`
      uni.redirectTo({ url })
    },
    deltaBack(delta) {
      uni.navigateBack({ delta })
    }
  }
}
