// dynamic product vector icons
export function getProductIcon(product) {
  if (product?.pic && !product.pic.includes('xxx') && product.pic.startsWith('http')) {
    return product.pic
  }

  const name = (product?.name || product?.productName || '').toLowerCase()
  const cat = (product?.categoryName || '').toLowerCase()
  const catId = Number(product?.categoryId || product?.category_id || 0)

  // phone
  if (catId === 7 || name.includes('phone') || name.includes('iphone') || name.includes('mate') || name.includes('xiaomi') || name.includes('galaxy')) {
    return 'data:image/svg+xml;utf8,<svg xmlns="http://www.w3.org/2000/svg" viewBox="0 0 100 100" fill="none"><rect width="100" height="100" rx="18" fill="%23282828"/><rect x="32" y="16" width="36" height="68" rx="6" stroke="%23FFCC00" stroke-width="3" fill="%231a1a1a"/><circle cx="50" cy="76" r="3" fill="%23FFCC00"/><line x1="44" y1="22" x2="56" y2="22" stroke="%23666" stroke-width="2" stroke-linecap="round"/><rect x="36" y="27" width="28" height="42" fill="%23222" rx="2"/></svg>'
  }

  // computer laptop
  if (catId === 8 || cat.includes('compute') || name.includes('mac') || name.includes('laptop') || name.includes('legion') || name.includes('core') || name.includes('accelerator')) {
    return 'data:image/svg+xml;utf8,<svg xmlns="http://www.w3.org/2000/svg" viewBox="0 0 100 100" fill="none"><rect width="100" height="100" rx="18" fill="%23282828"/><rect x="24" y="24" width="52" height="36" rx="3" stroke="%232F80ED" stroke-width="3" fill="%231a1a1a"/><polygon points="16,68 84,68 80,64 20,64" fill="%23444"/><line x1="16" y1="68" x2="84" y2="68" stroke="%232F80ED" stroke-width="3" stroke-linecap="round"/><circle cx="50" cy="42" r="6" fill="%232F80ED" opacity="0.4"/></svg>'
  }

  // tablet
  if (name.includes('tablet') || name.includes('pad')) {
    return 'data:image/svg+xml;utf8,<svg xmlns="http://www.w3.org/2000/svg" viewBox="0 0 100 100" fill="none"><rect width="100" height="100" rx="18" fill="%23282828"/><rect x="25" y="18" width="50" height="64" rx="5" stroke="%2300B843" stroke-width="3" fill="%231a1a1a"/><circle cx="50" cy="74" r="2.5" fill="%2300B843"/><rect x="30" y="26" width="40" height="42" fill="%23222" rx="2"/></svg>'
  }

  // audio
  if (name.includes('earbud') || name.includes('headphone') || name.includes('audio') || name.includes('sound') || name.includes('bluetooth')) {
    return 'data:image/svg+xml;utf8,<svg xmlns="http://www.w3.org/2000/svg" viewBox="0 0 100 100" fill="none"><rect width="100" height="100" rx="18" fill="%23282828"/><path d="M28 52 C28 35 72 35 72 52" stroke="%23F5A623" stroke-width="3" stroke-linecap="round" fill="none"/><rect x="22" y="50" width="12" height="20" rx="4" fill="%23F5A623"/><rect x="66" y="50" width="12" height="20" rx="4" fill="%23F5A623"/></svg>'
  }

  // keyboard mouse input
  if (catId === 9 || cat.includes('peripheral') || name.includes('keyboard') || name.includes('stylus') || name.includes('mouse')) {
    return 'data:image/svg+xml;utf8,<svg xmlns="http://www.w3.org/2000/svg" viewBox="0 0 100 100" fill="none"><rect width="100" height="100" rx="18" fill="%23282828"/><rect x="18" y="32" width="64" height="36" rx="4" stroke="%23FFCC00" stroke-width="3" fill="%231a1a1a"/><line x1="26" y1="42" x2="34" y2="42" stroke="%23FFCC00" stroke-width="2.5"/><line x1="42" y1="42" x2="50" y2="42" stroke="%23FFCC00" stroke-width="2.5"/><line x1="58" y1="42" x2="66" y2="42" stroke="%23FFCC00" stroke-width="2.5"/><line x1="74" y1="42" x2="74" y2="42" stroke="%23FFCC00" stroke-width="2.5"/><line x1="32" y1="56" x2="68" y2="56" stroke="%23FFCC00" stroke-width="2.5"/></svg>'
  }

  // watch sensor optics
  if (catId === 10 || cat.includes('wearable') || name.includes('glass') || name.includes('watch') || name.includes('touch') || name.includes('sensor')) {
    return 'data:image/svg+xml;utf8,<svg xmlns="http://www.w3.org/2000/svg" viewBox="0 0 100 100" fill="none"><rect width="100" height="100" rx="18" fill="%23282828"/><circle cx="50" cy="50" r="22" stroke="%2300B843" stroke-width="3" fill="%231a1a1a"/><line x1="50" y1="50" x2="50" y2="36" stroke="%2300B843" stroke-width="2.5" stroke-linecap="round"/><line x1="50" y1="50" x2="60" y2="50" stroke="%2300B843" stroke-width="2.5" stroke-linecap="round"/><rect x="42" y="16" width="16" height="12" fill="%23444" rx="2"/><rect x="42" y="72" width="16" height="12" fill="%23444" rx="2"/></svg>'
  }

  // network router
  if (cat.includes('network') || cat.includes('optic') || name.includes('router') || name.includes('hub') || name.includes('fiber')) {
    return 'data:image/svg+xml;utf8,<svg xmlns="http://www.w3.org/2000/svg" viewBox="0 0 100 100" fill="none"><rect width="100" height="100" rx="18" fill="%23282828"/><rect x="20" y="44" width="60" height="26" rx="4" stroke="%232F80ED" stroke-width="3" fill="%231a1a1a"/><line x1="32" y1="44" x2="32" y2="24" stroke="%232F80ED" stroke-width="2.5" stroke-linecap="round"/><line x1="68" y1="44" x2="68" y2="24" stroke="%232F80ED" stroke-width="2.5" stroke-linecap="round"/><circle cx="36" cy="57" r="2.5" fill="%2300B843"/><circle cx="50" cy="57" r="2.5" fill="%2300B843"/><circle cx="64" cy="57" r="2.5" fill="%2300B843"/></svg>'
  }

  // crypto vault key
  if (cat.includes('security') || name.includes('key') || name.includes('vault') || name.includes('encrypt')) {
    return 'data:image/svg+xml;utf8,<svg xmlns="http://www.w3.org/2000/svg" viewBox="0 0 100 100" fill="none"><rect width="100" height="100" rx="18" fill="%23282828"/><path d="M50 24 L72 32 V52 C72 66 50 76 50 76 C50 76 28 66 28 52 V32 Z" stroke="%23F03838" stroke-width="3" fill="%231a1a1a"/><circle cx="50" cy="46" r="4" fill="%23F03838"/><path d="M50 50 V58" stroke="%23F03838" stroke-width="2.5" stroke-linecap="round"/></svg>'
  }

  // apparel
  if ((catId >= 11 && catId <= 14) || cat.includes('apparel') || cat.includes('clothing') || name.includes('shirt') || name.includes('t-shirt') || name.includes('jeans') || name.includes('jacket') || name.includes('sneaker')) {
    return 'data:image/svg+xml;utf8,<svg xmlns="http://www.w3.org/2000/svg" viewBox="0 0 100 100" fill="none"><rect width="100" height="100" rx="18" fill="%23282828"/><path d="M35 24 L20 38 L30 46 L35 40 V76 H65 V40 L70 46 L80 38 L65 24 C60 30 40 30 35 24 Z" stroke="%23F5A623" stroke-width="3" fill="%231a1a1a"/></svg>'
  }

  // hardware fallback
  return 'data:image/svg+xml;utf8,<svg xmlns="http://www.w3.org/2000/svg" viewBox="0 0 100 100" fill="none"><rect width="100" height="100" rx="18" fill="%23282828"/><rect x="28" y="28" width="44" height="44" rx="8" stroke="%23FFCC00" stroke-width="3" fill="%231a1a1a"/><path d="M50 38 L50 62 M38 50 L62 50" stroke="%23FFCC00" stroke-width="3" stroke-linecap="round"/><circle cx="50" cy="50" r="5" fill="%23FFCC00"/></svg>'
}
