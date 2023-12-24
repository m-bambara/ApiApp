package jp.techacademy.motoyoshi.apiapp

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import jp.techacademy.motoyoshi.apiapp.databinding.ActivityWebViewBinding


class WebViewActivity : AppCompatActivity() {
    private lateinit var binding: ActivityWebViewBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityWebViewBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.webView.loadUrl(intent.getStringExtra(KEY_URL).toString())

        // 星アイコンにリスナーを設定
        binding.imageViewFavorite.setOnClickListener {
            toggleFavorite()
        }

        // idを取得し、そのidがFavoriteShopにあるかどうかで星の画像を変える(初期設定)
        val id = intent.getStringExtra(KEY_ID) ?: ""
        updateFavoriteIcon(id)
    }

    companion object {
        private const val KEY_ID = "key_id"
        private const val KEY_IMAGE_URL = "key_image_url"
        private const val KEY_NAME = "key_name"
        private const val KEY_URL = "key_url"
        //OnClickでMain空渡した引数をすべてインテント
        //複数の引数があるため、まとめて最後にStartActivityでインテント
        fun start(activity: Activity, id: String, imageUrl: String, name: String, url: String) {
            val intent = Intent(activity, WebViewActivity::class.java).apply {
                putExtra(KEY_ID, id)
                putExtra(KEY_IMAGE_URL, imageUrl)
                putExtra(KEY_NAME, name)
                putExtra(KEY_URL, url)
            }
            activity.startActivity(intent)
        }


    }

    //FavStarの状態を設定
    private fun updateFavoriteIcon(id: String) {
        val isFavorite = FavoriteShop.findBy(id) != null
        binding.imageViewFavorite.setImageResource(
            if (isFavorite) R.drawable.ic_star else R.drawable.ic_star_border
        )
    }

    //星を押したときの動きを定義
    private fun toggleFavorite() {
        // IntentからIDを取得
        val id = intent.getStringExtra(KEY_ID) ?: ""

        // お気に入りの状態を確認
        val isFavorite = FavoriteShop.findBy(id) != null

        // FavpriteShopにidが存在するかどうかで場合分け
        if (isFavorite) {
            // お気に入り解除のロジック <= Favotite.ktにメソッド定義
            FavoriteShop.delete(id)
            binding.imageViewFavorite.setImageResource(R.drawable.ic_star_border)
        } else {
            // お気に入り追加のロジック
            val imageUrl = intent.getStringExtra(KEY_IMAGE_URL) ?: ""
            val name = intent.getStringExtra(KEY_NAME) ?: ""
            val url = intent.getStringExtra(KEY_URL) ?: ""

            val favoriteShop = FavoriteShop(id, imageUrl, name, url)
            FavoriteShop.insert(favoriteShop)
            binding.imageViewFavorite.setImageResource(R.drawable.ic_star)
        }
    }

}



