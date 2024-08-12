# 画面内イベント一覧

# ランドマーク一覧画面

| イベント名 | Analytics イベント名 | パラメータ | コンバージョンイベント |
| -- | -- | -- | -- |
| いいねを付けたランドマークのみを表示するスイッチ | LandmarkListFavoritesOnlySwitch | スイッチの ON/OFF favorites_only: Boolean<br> |  |

# ランドマーク詳細画面

| イベント名 | Analytics イベント名 | パラメータ | コンバージョンイベント |
| -- | -- | -- | -- |
| いいねを付ける | LandmarkDetailFavoriteOn | ランドマークの ID id: Long<br>ランドマークの名前 name: String<br> | ○ |
| いいねを解除する | LandmarkDetailFavoriteOff | ランドマークの ID id: Long<br>ランドマークの名前 name: String<br> |  |

# 情報画面

| イベント名 | Analytics イベント名 | パラメータ | コンバージョンイベント |
| -- | -- | -- | -- |
| プライバシーポリシーを表示する | InfoPrivacyPolicy |  |  |

