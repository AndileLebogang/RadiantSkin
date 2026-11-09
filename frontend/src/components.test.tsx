import { afterEach, describe, expect, it } from 'vitest'
import { cleanup, fireEvent, render, screen } from '@testing-library/react'
import { ProductImage } from './components'
import { normalizeProduct } from './lib/store'
import { productImageFallback } from './lib/productImages'

afterEach(cleanup)

describe('Product images', () => {
  it('recovers from a failed URL and tries a changed product URL', () => {
    const product = normalizeProduct({
      productId: 1,
      name: 'Serum',
      price: 20,
      imageUrl: 'https://store.example/expired.jpg',
    })
    const { rerender } = render(<ProductImage product={product} />)
    fireEvent.error(screen.getByRole('img'))
    expect(screen.getByRole('img').getAttribute('src')).toBe(productImageFallback)
    expect(screen.getByRole('img').getAttribute('alt')).toContain('image unavailable')
    rerender(
      <ProductImage product={{ ...product, imageUrl: '/images/products/replacement.jpg' }} />,
    )
    expect(screen.getByRole('img').getAttribute('src')).toBe('/images/products/replacement.jpg')
  })

  it('renders a local fallback for missing images and stops retrying if that fails too', () => {
    const product = normalizeProduct({ productId: 1, name: 'Serum', price: 20 })
    render(<ProductImage product={product} />)
    expect(screen.getByRole('img').getAttribute('src')).toBe(productImageFallback)
    fireEvent.error(screen.getByRole('img'))
    expect(screen.queryByRole('img')).toBeNull()
    expect(screen.getByText('Image coming soon')).toBeTruthy()
  })
})
