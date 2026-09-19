# MoreIotas — Hex Casting pre53

Minecraft 1.21.1 · NeoForge 21.1.234 · Java 21 · **Hex Casting `0.12.0-devel-pre-53` exactly**.

Unofficial source port; upstream `0d96e56c3cff5657cdeeb8394ba7b0c9c6a48387` from https://github.com/FallingColors/MoreIotas.git. Original authorship/licenses retained. Source branch: `pre53`; `pre39` remains unchanged.

## 本批核心结果与覆盖边界

### moreiotas
**已通过（有界核心链）**：真实 ServerPlayer 环境的标准 mul 得到 2×2 正确矩阵；非法维度走 VM Mishap；公开 Focus 写入、原生 S2C、同世界新双端 JVM 只读回读及完整库存磁盘一致均通过。
**本行代表性核心缺口已关闭**：未发现矩阵产品缺陷；更多运算/维度排列属补充，不升级阻断。

## Artifacts

| File | SHA-256 |
|---|---|
| `moreiotas-0.1.1+1.21.1-neoforge-pre53.jar` | `04fd53fddfa8c21a448becd25ec0692c02dc076ddfc745eaa700705fd9474e23` |

Install the dependencies declared in [PORTING.md](https://github.com/FluorineUCK/MoreIotas-NeoForge-1.21.1/blob/pre53/PORTING.md). Independent test probes, API/source JARs and third-party dependency JARs are not release assets. Source builds require the declared local/sibling inputs; no standalone offline-build claim is made.
