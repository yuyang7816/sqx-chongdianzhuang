/**
 * 单位操作
 */
export const complete_unit = (num) => (/^[0-9]*$/.test(num) ? num + 'px' : num)
export const remove_unit = numUnit => /^[0-9]*$/.test(numUnit) ? numUnit : /[0-9]*/.exec(numUnit)[0]

/**
 * uni get DOM
 */
export const getRect = (_this, identification) => new Promise((resolve, reject) => {
  uni.createSelectorQuery().in(_this).select(identification).boundingClientRect(data => resolve(data)).exec()
})

export const ObjToStringQuery = (params) =>
  Object.entries(params)
    .map((param) => param.join('='))
    .join('&')
