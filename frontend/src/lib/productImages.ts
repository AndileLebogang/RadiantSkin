// Older catalogue imports used these reserved example.com URLs.
// Resolve them locally so existing databases work without a data migration.
const catalogueImages: Record<string, string> = {
  '/images/serum.jpg': '/images/products/radiantskin-hydrating-facial-serum-30ml.svg',
  '/images/hairmask.jpg': '/images/products/radiantskin-repairing-hair-mask-250ml.svg',
  '/images/lotion.jpg': '/images/products/radiantskin-soothing-body-lotion-400ml.svg',
}

export const productImageFallback = '/images/products/image-unavailable.svg'

export function resolveProductImage(value: string): string {
  const url = value.trim()
  try {
    const parsed = new URL(url)
    if (parsed.hostname.toLowerCase() === 'example.com')
      return catalogueImages[parsed.pathname] || ''
  } catch {
    // Relative image paths are validated below.
  }
  if (/^https?:\/\//i.test(url) || /^\/(?!\/)/.test(url)) return url
  return ''
}
