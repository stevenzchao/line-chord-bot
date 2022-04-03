/*
 * Copyright 2018 LINE Corporation
 *
 * LINE Corporation licenses this file to you under the Apache License,
 * version 2.0 (the "License"); you may not use this file except in compliance
 * with the License. You may obtain a copy of the License at:
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations
 * under the License.
 */

package linechrordbot.controller;

import static java.util.Arrays.asList;

import java.net.URI;
import java.util.function.Function;
import java.util.function.Supplier;

import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.linecorp.bot.model.action.PostbackAction;
import com.linecorp.bot.model.action.URIAction;
import com.linecorp.bot.model.message.FlexMessage;
import com.linecorp.bot.model.message.flex.component.Box;
import com.linecorp.bot.model.message.flex.component.Button;
import com.linecorp.bot.model.message.flex.component.Button.ButtonHeight;
import com.linecorp.bot.model.message.flex.component.Button.ButtonStyle;
import com.linecorp.bot.model.message.flex.component.Icon;
import com.linecorp.bot.model.message.flex.component.Image;
import com.linecorp.bot.model.message.flex.component.Image.ImageAspectMode;
import com.linecorp.bot.model.message.flex.component.Image.ImageAspectRatio;
import com.linecorp.bot.model.message.flex.component.Image.ImageSize;
import com.linecorp.bot.model.message.flex.component.Separator;
import com.linecorp.bot.model.message.flex.component.Spacer;
import com.linecorp.bot.model.message.flex.component.Text;
import com.linecorp.bot.model.message.flex.component.Text.TextWeight;
import com.linecorp.bot.model.message.flex.container.Bubble;
import com.linecorp.bot.model.message.flex.unit.FlexFontSize;
import com.linecorp.bot.model.message.flex.unit.FlexLayout;
import com.linecorp.bot.model.message.flex.unit.FlexMarginSize;

public class ChordFlexMessageSupplier implements Function<String,FlexMessage> {
    @Override
    public FlexMessage apply(String searchChord) {
        final Image heroBlock =
                Image.builder()
                     .url(ServletUriComponentsBuilder.fromCurrentContextPath().scheme("https").path("test.PNG").build().toUri())
                     .size(ImageSize.FULL_WIDTH)
                     .aspectRatio(ImageAspectRatio.R20TO13)
                     .aspectMode(ImageAspectMode.Cover)
                     .build();
        final Image heroBlock2 =
                Image.builder()
                     .url(ServletUriComponentsBuilder.fromCurrentContextPath().scheme("https").path("test2.PNG").build().toUri())
                     .size(ImageSize.FULL_WIDTH)
                     .aspectRatio(ImageAspectRatio.R20TO13)
                     .aspectMode(ImageAspectMode.Cover)
                     .build();
        
        final Box TestHeroBlock = Box.builder()
                .layout(FlexLayout.HORIZONTAL)
                .contents(asList(heroBlock, heroBlock2))
                .build();

        final Box headerBlock = createHeaderBlock();
        final Box bodyBlock = createBodyBlock();
        final Box footerBlock = createFooterBlock();
        final Bubble bubble =
                Bubble.builder()
                	 .header(headerBlock)
                      .hero(TestHeroBlock)
                      .body(bodyBlock)
//                      .footer(footerBlock)
                      .build();

        return new FlexMessage("Chord~~", bubble);
    }

    private Box createFooterBlock() {
        final Spacer spacer = Spacer.builder().size(FlexMarginSize.SM).build();
//        final Button callAction = Button
//                .builder()
//                .style(ButtonStyle.LINK)
//                .height(ButtonHeight.SMALL)
//                .action(new URIAction("CALL", URI.create("tel:000000"), null))
//                .build();
        final Separator separator = Separator.builder().build();
        final Button websiteAction =
                Button.builder()
                      .style(ButtonStyle.LINK)
                      .height(ButtonHeight.SMALL)
                      .action(new URIAction("WEBSITE", URI.create("https://youtube.com"), null))
                      .build();
        final Button postBackAction =
                Button.builder()
                      .style(ButtonStyle.LINK)
                      .height(ButtonHeight.SMALL)
                      .action(new PostbackAction("和弦查詢", "和弦查詢", "和弦查詢"))
                      .build();

        return Box.builder()
                  .layout(FlexLayout.VERTICAL)
                  .spacing(FlexMarginSize.SM)
                  .contents(asList(spacer, websiteAction, separator, postBackAction))
                  .build();
    }
    
    private Box createHeaderBlock() {
        final Text title =
                Text.builder()
                    .text("Cm7 小七和弦")
                    .weight(TextWeight.BOLD)
                    .size(FlexFontSize.XL)
                    .build();
        return Box.builder()
                  .layout(FlexLayout.VERTICAL)
                  .contents(asList(title))
                  .build();
    }

    private Box createBodyBlock() {
        final Box info = createInfoBox();
        return Box.builder()
                  .layout(FlexLayout.VERTICAL)
                  .contents(asList(info))
                  .build();
    }

    private Box createInfoBox() {
   	
        final Box constructedNotes = Box
                .builder()
                .layout(FlexLayout.BASELINE)
                .spacing(FlexMarginSize.SM)
                .contents(asList(
                        Text.builder()
                            .text("組成音：")
                            .wrap(true)
                            .color("#666666")
                            .size(FlexFontSize.SM)
                            .flex(2)
                            .build(),
                            Text.builder()
                            .text("C E G B")
                            .wrap(true)
                            .color("#aaaaaa")
                            .size(FlexFontSize.SM)
                            .build()
                ))
                .build();
        
        final Box simpleConstructedNotes = Box
                .builder()
                .layout(FlexLayout.BASELINE)
                .spacing(FlexMarginSize.SM)
                .contents(asList(
                        Text.builder()
                            .text("簡譜組成音：")
                            .wrap(true)
                            .color("#666666")
                            .size(FlexFontSize.SM)
                            .flex(2)
                            .build(),
                            Text.builder()
                            .text("1 3 5 7")
                            .wrap(true)
                            .color("#aaaaaa")
                            .size(FlexFontSize.SM)
                            .build()
                ))
                .build();
        
        final Box relativeSimpleConstructedNotes = Box
                .builder()
                .layout(FlexLayout.BASELINE)
                .spacing(FlexMarginSize.SM)
                .contents(asList(
                        Text.builder()
                            .text("相對音程(根音為1)：")
                            .wrap(true)
                            .color("#666666")
                            .size(FlexFontSize.SM)
                            .flex(2)
                            .build(),
                            Text.builder()
                            .text("1 3 5 7")
                            .wrap(true)
                            .color("#aaaaaa")
                            .size(FlexFontSize.SM)
                            .build()
                ))
                .build();

        return Box.builder()
                  .layout(FlexLayout.VERTICAL)
                  .margin(FlexMarginSize.LG)
                  .spacing(FlexMarginSize.SM)
                  .contents(asList(constructedNotes,simpleConstructedNotes,relativeSimpleConstructedNotes))
                  .build();
    }

    private Box createReviewBox() {
        final Icon goldStar =
                Icon.builder().size(FlexFontSize.SM).url(URI.create("https://example.com/gold_star.png")).build();
        final Icon grayStar =
                Icon.builder().size(FlexFontSize.SM).url(URI.create("https://example.com/gray_star.png")).build();
        final Text point =
                Text.builder()
                    .text("4.0")
                    .size(FlexFontSize.SM)
                    .color("#999999")
                    .margin(FlexMarginSize.MD)
                    .flex(0)
                    .build();

        return Box.builder()
                  .layout(FlexLayout.BASELINE)
                  .margin(FlexMarginSize.MD)
                  .contents(asList(goldStar, goldStar, goldStar, goldStar, grayStar, point))
                  .build();
    }
}
