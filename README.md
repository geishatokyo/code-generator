# Code generator

コードジェネレーションをサポートするためのライブラリです。

* DSLのパーサー
* ファイルマージ、置換サポート

の機能があります。



## 置換マーカー

マージルールは、含まれるマーカーの種類により決まります
説明より、/src/test/scala/com/geishatokyo/codegen/replacer/MergeTest.scala
のテストコードを読んで貰ったほうがわかりやすいと思います。

### 全体置換モード

トップレベルのマーカーが

* \##hold ~ ##end
* \##insteadOf ~ ##insert ~ ##end

の場合、この全体置換モードになります。
この場合、\##holdマーカーの内側のみが残り、その外側が全て置換されます。
\##insteadOfマーカーの場合は、insteadOf ~ insert間に完全一致する部分が存在した場合に、その場所にinsteadOf ~ insert ~ endブロックをそのまま挿入します。



### 部分置換モード

トップレベルのマーカーが

* \##replace ~ ##end
* \##insert

の場合、部分置換モードとなります。
replaceマーカの内部のみが置換され、その外側に関してはそのまま残ります。
insertマーカーは、同じ名前をとったreplaceマーカーの内部を、その位置に挿入します。




